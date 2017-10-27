package CodigosJppf;

import java.util.List;
import static javax.management.Query.gt;
import static javax.management.Query.lt;
import org.jppf.client.JPPFClient;
import org.jppf.client.JPPFJob;
import org.jppf.client.JPPFResultCollector;
import org.jppf.node.protocol.Task;
import org.jppf.server.protocol.JPPFTask; 

public class TemplateApplicationRunner {
 
 private static JPPFClient jppfClient = null;
    JPPFJob Job = new JPPFJob();
 
 public static void main(final String... args) {
    try {
       jppfClient = new JPPFClient();
       TemplateApplicationRunner runner = new TemplateApplicationRunner();
      // Criação da JOB
       JPPFJob job = runner.createJob();
      // execução do bloco da JOB
       runner.executeBlockingJob(job);
    } 
    catch (Exception e) 
    {
       e.printStackTrace();
    } 
    finally 
    {
       if (jppfClient != null) 
       {
            jppfClient.close();
       }
    }
 }
//metodo onde cria a JOB public JPPF

 JPPFJob createJob() throws Exception 
 {
    JPPFJob job = new JPPFJob();
    job.setName("Template Job Id");
    job.add(new TemplateJPPFTask());
    //job.addTask(new TemplateJPPFTask());
    return job;
 }
//executa a JOB 
 
 public void executeBlockingJob(final JPPFJob job) throws Exception 
 {
     List<Task<?>> results = null; 
     
    job.setBlocking(true);
    /*List & lt;
    JPPFTask & gt;*/
    results = jppfClient.submitJob(job);
    //results = jppfClient.submit(job);
    processExecutionResults(results);
 }
//Executa o NO 
 
 public void executeNonBlockingJob(final JPPFJob job) throws Exception 
 {
     List<Task<?>> results;
     
    job.setBlocking(false);
    JPPFResultCollector collector = submitNonBlockingJob(job);
    //List & lt;
    //JPPFTask & gt;
    //results = collector.waitForResults();
    results  = collector.awaitResults();
    processExecutionResults(results);
 }
 
 public JPPFResultCollector submitNonBlockingJob(final JPPFJob job) throws Exception 
 {
    job.setBlocking(false);
    JPPFResultCollector collector = new JPPFResultCollector(job);
    job.setResultListener(collector);
    //jppfClient.submit(job);
    jppfClient.submitJob(job);
    return collector;
 }
 
 public void processExecutionResults(final List<Task<?>> results ) 
 {
    for (Task<?> task : results) {
        if (task.getException() != null) {

        } 
        else 
        {
            //vazio
        }
    }
 }
}