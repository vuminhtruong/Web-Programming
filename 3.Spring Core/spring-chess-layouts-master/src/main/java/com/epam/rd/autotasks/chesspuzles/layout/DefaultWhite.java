package com.epam.rd.autotasks.chesspuzles.layout;

import com.epam.rd.autotasks.chesspuzles.Cell;
import com.epam.rd.autotasks.chesspuzles.ChessPiece;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;

import java.io.*;

@Configuration
public class DefaultWhite {
    @Autowired
    private ConfigurableBeanFactory beanFactory;

    @Bean
    public void beanForDefaultBlack() throws IOException {
        File file = new File("src/test/resources/boards/DefaultWhite.txt");
        BufferedReader reader = new BufferedReader(new FileReader(file));
        int row = 8;
        int id = 0;
        while(row > 0){
            String line = reader.readLine();
            if(line == null){
                break;
            }
            for(char letter = 'A';letter <= 'H';++letter){
                int col = letter - 'A';
                final int tmp_row = row;
                final char tmp_letter = letter;
                if(line.charAt(col) != '.'){
                    beanFactory.registerSingleton("ID" + ++id,
                            new ChessPiece() {
                                @Override
                                public Cell getCell() {
                                    return Cell.cell(tmp_letter,tmp_row);
                                }

                                @Override
                                public char toChar() {
                                    return line.charAt(col);
                                }
                            });
                }
            }
            row--;
        }
    }
}
