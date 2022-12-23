package com.epam.rd.autotasks.confbeans.config;

import com.epam.rd.autotasks.confbeans.video.Channel;
import com.epam.rd.autotasks.confbeans.video.Video;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.time.LocalDateTime;
import java.util.stream.Stream;

@Configuration
public class ChannelWithInjectedPrototypeVideoConfig extends Channel {

    private static LocalDateTime time = LocalDateTime.of(2001,10,18,10,0);

    private Video createVideo(){
        Video video = new Video("Cat Failure Compilation",time);
        time = time.plusDays(1);
        return video;
    }

    @Override
    public Stream<Video> videos() {
        Channel channel = new Channel();
        for(int days = 1;days <= 7;++days){
            channel.addVideo(createVideo());
        }
        return channel.videos();
    }

    @Bean("channel")
    public Channel beanChannel(){
        return new ChannelWithInjectedPrototypeVideoConfig();
    }

    @Bean
    @Scope("prototype")
    public Video beanVideo(){
        return createVideo();
    }
}
