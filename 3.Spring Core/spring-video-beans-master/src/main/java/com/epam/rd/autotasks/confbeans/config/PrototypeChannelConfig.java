package com.epam.rd.autotasks.confbeans.config;

import com.epam.rd.autotasks.confbeans.video.Channel;
import com.epam.rd.autotasks.confbeans.video.Video;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.time.LocalDateTime;

@Configuration
public class PrototypeChannelConfig {

    @Bean
    @Scope("prototype")
    Channel beanchannel(){
        Channel channel = new Channel();
        channel.addVideo(beanvideo1());
        channel.addVideo(beanvideo2());
        channel.addVideo(beanvideo3());
        return channel;
    }

    @Bean("video1")
    Video beanvideo1(){
        return new Video("How to boil water", LocalDateTime.of(2020,10,10,10,10));
    }

    @Bean
    Video beanvideo2(){
        return new Video("How to build a house",LocalDateTime.of(2020,10,10,10,11));
    }

    @Bean
    Video beanvideo3(){
        return new Video("How to escape solitude",LocalDateTime.of(2020,10,10,10,12));
    }
}
