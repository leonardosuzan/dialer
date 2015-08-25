package dialer;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.MapJobRepositoryFactoryBean;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {
	
	
	// tag::readerwriterprocessor[]
	@StepScope
	@Bean
	public ItemReader<Contato> reader(@Value("#{jobParameters[fileName]}") String fileName, @Value("#{jobParameters[listingId]}") Long listingId) {
		FlatFileItemReader<Contato> reader = new FlatFileItemReader<Contato>();
		reader.setResource(new FileSystemResource(fileName));
		reader.setLineMapper(new DefaultLineMapper<Contato>() {
			{
				setLineTokenizer(new DelimitedLineTokenizer() {
					{
						setNames(new String[] { "primeiroNome", "sobrenome",
								"codigoArea", "numeroTelefonico", "cidade","estado", "endereco",
								"numeroEndereco", "complemento", "bairro",
								  "observacoes" });
					}
				});
				setFieldSetMapper(new ContatoFieldSetMapper());
			}
		});
		
		
//		System.out.println("#{jobParameters[listingId]}");
		
		reader.open(new ExecutionContext());
		
		return reader;
	}

	@Bean
	public ItemProcessor<Contato, Contato> processor() {
		return new ContatoItemProcessor();
	}

	@Bean
	@StepScope
	public ItemWriter<Contato> writer(DataSource dataSource, @Value("#{jobParameters[listingId]}") Long listingId) {
		
		System.out.println("Importando contatos para listagem <ID>: "+listingId);
		
		JdbcBatchItemWriter<Contato> writer = new JdbcBatchItemWriter<Contato>();
		writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Contato>());
		writer.setSql("INSERT INTO contatosListagens (idListagens,primeiroNome,sobrenome,ddd,numeroTelefonico,endereco,numero,complemento,bairro,cidade,estado,observacoes) VALUES ("+listingId+",:primeiroNome,:sobrenome,:codigoArea,:numeroTelefonico,:endereco,:numeroEndereco,:complemento,:bairro,:cidade,:estado,:observacoes)");
		writer.setDataSource(dataSource);
		return writer;
	}

	// end::readerwriterprocessor[]

	// tag::jobstep[]
	@Bean
	public Job importContactsJob(JobBuilderFactory jobs, Step s1,
			JobExecutionListener listener) {
		return jobs.get("importContactsJob")
				.incrementer(new RunIdIncrementer()).listener(listener)
				.flow(s1).end().build();
	}

	@Bean
	public Step step1(StepBuilderFactory stepBuilderFactory,
			ItemReader<Contato> reader, ItemWriter<Contato> writer,
			ItemProcessor<Contato, Contato> processor) {
		return stepBuilderFactory.get("step1").<Contato, Contato> chunk(10)
				.reader(reader).processor(processor).writer(writer).build();
	}

	// end::jobstep[]

	@Bean
	public JdbcTemplate jdbcTemplate(DataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}

	@Bean
	public JobLauncher getJobLauncher() throws Exception {
		SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
		jobLauncher.setJobRepository(getJobRepository());
		jobLauncher.afterPropertiesSet();
		return jobLauncher;
	}


	public JobRepository getJobRepository() throws Exception {
		MapJobRepositoryFactoryBean factory = new MapJobRepositoryFactoryBean();
		factory.setTransactionManager(new ResourcelessTransactionManager());
		factory.afterPropertiesSet();
		return (JobRepository) factory.getObject();
	}
	
//	  public void setFileName(final String name) {
//	    this.fileName=name;
//	    return;
//	  }
}
