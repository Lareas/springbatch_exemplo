package com.udemy.primeirojobspringbatch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.function.FunctionItemProcessor;
import org.springframework.batch.item.support.IteratorItemReader;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@EnableBatchProcessing
@Configuration
public class PrimeiroJobSpringBatchConfig {
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Bean
	public Job imprimeParouImpar() {
		return jobBuilderFactory
				.get("imprimeParouImpar")
				.start(imprimeParouImparStep())
				.incrementer(new RunIdIncrementer()).build();
	}

	private Step imprimeParouImparStep() {
		return stepBuilderFactory
				.get("imprimeParouImparStep")
				.<Integer, String>chunk(1)
				.reader(contaAteDezReader())
				.processor(parOuImparProcessor())
				.writer(imprimeWriter())
				.build();
	}

	public IteratorItemReader<Integer> contaAteDezReader() {
		List<Integer> numerosAteDez = Arrays.asList(1,2,3,4,5,6,7,8,9,10);
		return new IteratorItemReader<Integer>(numerosAteDez.iterator());
	}

	public FunctionItemProcessor<Integer, String> parOuImparProcessor() {
		return new FunctionItemProcessor<Integer, String>
				(item -> item % 2 == 0 ? String.format("Item %s é Par", item) : String.format("Item %s é Ímpar", item));
	}

	public ItemWriter<String> imprimeWriter() {
		return itens -> itens.forEach(System.out::println);
	}

/*
	public Step imprimeOlaStep() {
		return stepBuilderFactory.get("imprimeOlaStep")
				.tasklet(imprimeOlaTasklet(null))
				.build();
	}

	@Bean
	@StepScope
	public Tasklet imprimeOlaTasklet(@Value("#{jobParameters['nome']}") String nome) {
		return new Tasklet() {

			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
				System.out.println(String.format("Olá, " + nome));
				return RepeatStatus.FINISHED;
			}
		};
	}

	@Bean
	public Job imprimeOlaJob() {
		return jobBuilderFactory.get("imprimeOlaJob")
				.start(imprimeOlaStep())
				.incrementer(new RunIdIncrementer())
				.build();
	}
	*/

}
