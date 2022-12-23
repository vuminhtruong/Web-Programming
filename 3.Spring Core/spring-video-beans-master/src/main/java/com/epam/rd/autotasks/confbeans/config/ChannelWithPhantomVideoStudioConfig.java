package com.epam.rd.autotasks.confbeans.config;

import com.epam.rd.autotasks.confbeans.video.Channel;
import com.epam.rd.autotasks.confbeans.video.Video;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.time.LocalDateTime;

@Configuration
public class ChannelWithPhantomVideoStudioConfig {
    private static Channel channel = new Channel();
    private static int season = 1;
    private static LocalDateTime time = LocalDateTime.of(2001,10,18,10,0);

    private Video createVideo(){
        Video video = new Video("Cat & Curious " + season,time);
        time = time.plusYears(2);
        season++;
        return video;
    }

    @Bean
    public Channel beanChannel(){
        while(season <= 8){
            channel.addVideo(createVideo());
        }
        return channel;
    }

    @Bean
    @Scope("prototype")
    public Video beanVideo(){
        return createVideo();
    }
}
