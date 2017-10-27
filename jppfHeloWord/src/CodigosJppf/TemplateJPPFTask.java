package CodigosJppf;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jppf.node.protocol.AbstractTask;
 
class TemplateJPPFTask extends AbstractTask <String>{
/*este metodo voce pode colocar o que voce deseja executar, como exemplo eu coloquei para abrir o blog e uma aplicação que nossa equipe desenvolveu porem voce pode implementar o que desejar*/
 
    public void run() 
    {
        try 
        {
            Runtime.getRuntime().exec("C:\\Program Files (x86)\\Mozilla Firefox\\firefox.exe -new-tab https://cpdjppf.wordpress.com");
        } catch (IOException ex) 
        {
            Logger.getLogger(TemplateJPPFTask.class.getName()).log(Level.SEVERE, null, ex);
        }
        try 
        {
            Runtime.getRuntime().exec("java -jar \"C:\\Users\\Diogo Anphiloquio\\Documents\\NetBeansProjects\\MaquinaTuring\\dis \\MaquinaTuring.jar\"");
        } catch (IOException ex) 
        {
            Logger.getLogger(TemplateJPPFTask.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

 

