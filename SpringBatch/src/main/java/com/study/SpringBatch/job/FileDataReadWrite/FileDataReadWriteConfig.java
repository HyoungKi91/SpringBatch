package com.study.SpringBatch.job.FileDataReadWrite;

import com.study.SpringBatch.job.FileDataReadWrite.dto.Player;
import com.study.SpringBatch.job.FileDataReadWrite.dto.PlayerYears;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import java.util.List;

/**
 * desc : 파일 읽고 저장
 * run : --spring.batch.job.names=fileReadWriteJob
 */
@Configuration
@RequiredArgsConstructor
public class FileDataReadWriteConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job fileReadWriteJob(Step fileReadWriteStep){
        return jobBuilderFactory.get("fileReadWriteJob")
                .incrementer(new RunIdIncrementer())
                .start(fileReadWriteStep)
                .build();
    }

    @JobScope
    @Bean
    public Step fileReadWriteStep(ItemReader playerFlatFileItemReader , ItemProcessor playerPlayerYearsItemProcessor , ItemWriter playerYearsFlatFileItemWriter){
        return stepBuilderFactory.get("fileReadWriteStep")
                .<Player, PlayerYears>chunk(5)
                .reader(playerFlatFileItemReader)
/*                .writer(new ItemWriter() {
                    @Override
                    public void write(List items) throws Exception {
                        items.forEach(System.out::println);
                    }
                })*/
                .processor(playerPlayerYearsItemProcessor)
                .writer(playerYearsFlatFileItemWriter)
                .build();
    }

    @StepScope
    @Bean
    public FlatFileItemWriter<PlayerYears> playerYearsFlatFileItemWriter(){
        BeanWrapperFieldExtractor<PlayerYears> playerYearsBeanWrapperFieldExtractor = new BeanWrapperFieldExtractor<>(); //사용할 필드를 명시하기 위해
        playerYearsBeanWrapperFieldExtractor.setNames(new String[]{"ID","lastName","position","yearsExperience"});
        playerYearsBeanWrapperFieldExtractor.afterPropertiesSet();

        DelimitedLineAggregator<PlayerYears> playerYearsDelimitedLineAggregator = new DelimitedLineAggregator<>();//어떤 기준으로 파일을 만들것인지
        playerYearsDelimitedLineAggregator.setDelimiter(","); //콤마로 구분하고
        playerYearsDelimitedLineAggregator.setFieldExtractor(playerYearsBeanWrapperFieldExtractor);//어떤 필드를 추출할것이냐

        FileSystemResource fileSystemResource = new FileSystemResource("players_output.txt");

        return new FlatFileItemWriterBuilder<PlayerYears>()
                .name("playerYearsFlatFileItemWriter")
                .resource(fileSystemResource)
                .lineAggregator(playerYearsDelimitedLineAggregator)
                .build();
    }

    @StepScope
    @Bean
    public ItemProcessor<Player,PlayerYears> playerPlayerYearsItemProcessor(){
        return new ItemProcessor<Player, PlayerYears>() {
            @Override
            public PlayerYears process(Player item) throws Exception {
                return new PlayerYears(item);
            }
        };
    }

    @StepScope
    @Bean
    public FlatFileItemReader<Player> playerFlatFileItemReader(){
        return new FlatFileItemReaderBuilder<Player>()
                .name("playerFlatFileItemReader")
                .resource(new FileSystemResource("Players.csv")) //resource의 담을 파일명
                .lineTokenizer(new DelimitedLineTokenizer()) //데이터를 나누어줄 기준 -- DelimitedLineTokenizer는 콤마 단위로 나누는것
                .fieldSetMapper(new PlayerFieldSetMapper())//읽어온 데이터를 객체로 변경할 수 있도록 mapper 사용
                .linesToSkip(1)//첫번째 줄은 skip 하겠다
                .build();
    }
}
