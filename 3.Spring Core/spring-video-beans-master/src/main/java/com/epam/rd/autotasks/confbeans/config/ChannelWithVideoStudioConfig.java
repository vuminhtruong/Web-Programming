package com.epam.rd.autotasks.confbeans.config;

import com.epam.rd.autotasks.confbeans.video.Channel;
import com.epam.rd.autotasks.confbeans.video.Video;
import com.epam.rd.autotasks.confbeans.video.VideoStudio;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Configuration
public class ChannelWithVideoStudioConfig {

    private static Channel channel = new Channel();
    private static int season = 1;
    private static LocalDateTime time = LocalDateTime.of(2001, 10, 18, 10, 0);

    private Video createVideo(){
        Video film = new Video("Cat & Curious " + season,time);
        time = time.plusYears(2);
        season++;
        return film;
    }

    @Bean
    public Channel beanChannel(){
        while (season<9){
            channel.addVideo(createVideo());
        }
        return channel;
    }

    @Bean
    VideoStudio beanvideoStudio(){
        return new VideoStudio() {
            @Override
            public Video produce() {
                channel.addVideo(createVideo());
                return channel.videos().collect(Collectors.toList()).get(season - 2);
            }
        };
    }
}
