package net.itbaima.robot.autoconfigure;

import jakarta.annotation.Resource;
import net.itbaima.robot.autoconfigure.RobotProperties.DataConfig;
import net.itbaima.robot.event.RobotEventPostProcessor;
import net.itbaima.robot.service.RobotService;
import net.itbaima.robot.service.impl.RobotServiceImpl;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.IMirai;
import net.mamoe.mirai.Mirai;
import net.mamoe.mirai.auth.BotAuthorization;
import net.mamoe.mirai.utils.BotConfiguration;
import net.mamoe.mirai.utils.LoggerAdapters;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import xyz.cssxsh.mirai.tool.sign.service.SignServiceConfig;
import xyz.cssxsh.mirai.tool.ProtocolVersionFixer;
import xyz.cssxsh.mirai.tool.sign.service.SignServiceFactory;

import java.io.File;

@Configuration
@Import(RobotEventPostProcessor.class)
@EnableConfigurationProperties({RobotProperties.class})
public class RobotAutoConfiguration {

    static {
        LoggerAdapters.useLog4j2();
    }

    @Resource
    RobotProperties properties;

    @Bean
    @ConditionalOnMissingBean
    public IMirai mirai(){
        return Mirai.getInstance();
    }

    @Bean
    public RobotService robotService(){
        return new RobotServiceImpl();
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(
            prefix = RobotProperties.PREFIX,
            name = "login-type",
            havingValue = "password"
    )
    public Bot createBotByPassword() {
        if(properties.getPassword() == null || properties.getUsername() == null)
            throw new RuntimeException("登录失败，请先配置QQ账号和密码");
        DataConfig data = properties.getData();
        this.createWorkDir(data.getWorkDir());
        this.fixProtocolVersion(data.getWorkDir());
        Bot bot = BotFactory.INSTANCE.newBot(
                properties.getUsername(), properties.getPassword(), this::configureRobot);
        bot.login();
        return bot;
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(
            prefix = RobotProperties.PREFIX,
            name = "login-type",
            havingValue = "qr_code",
            matchIfMissing = true
    )
    public Bot createBotByQrcode() {
        if(properties.getUsername() == null)
            throw new RuntimeException("登录失败，请先配置QQ账号");
        DataConfig data = properties.getData();
        createWorkDir(data.getWorkDir());
        Bot bot = BotFactory.INSTANCE.newBot(
                properties.getUsername(), BotAuthorization.byQRCode(), this::configureRobot);
        bot.login();
        return bot;
    }

    private void fixProtocolVersion(String path){
        RobotProperties.SignerConfig signer = properties.getSigner();
        SignServiceFactory.initConfiguration(path, new SignServiceConfig(
                signer.getUrl(),
                signer.getType().toName(),
                signer.getKey(),
                signer.getServerIdentityKey(),
                signer.getAuthorizationKey()
        ));
        SignServiceFactory.install();
        ProtocolVersionFixer.fetch(properties.getProtocol(), signer.getVersion());
    }

    private void configureRobot(BotConfiguration configuration) {
        DataConfig data = properties.getData();
        configuration.setWorkingDir(new File(data.getWorkDir()));
        configuration.setCacheDir(new File(data.getCacheDir()));
        configuration.setHeartbeatStrategy(properties.getStrategy());
        configuration.setProtocol(properties.getProtocol());
        if(data.isSaveDeviceId()) {
            configuration.fileBasedDeviceInfo("robot-device.json");
        }
        if(data.isContactCache()) {
            configuration.enableContactCache();
        } else {
            configuration.disableContactCache();
        }
    }

    private void createWorkDir(String path) {
        File dir = new File(path);
        if(!dir.exists()) {
            if(!dir.mkdirs())
                throw new RuntimeException("无法完成机器人启动，创建工作目录失败");
        }
    }
}
