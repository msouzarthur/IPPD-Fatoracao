import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.Scanner;

public class FatCliente{
    public static void main(String[] args){
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            FatServico server = (FatServico) registry.lookup("FatServico");
            Scanner scanner = new Scanner(System.in);
            String entrada;
            long numero;
            List<Long> factors; 

            while(true){
                System.out.println("> digite um numero ou 'sair'");
                System.out.print("< ");
                entrada = scanner.nextLine();

                if(entrada.equals("sair")){
                    break;
                }

                try{
                    numero=Long.parseLong(entrada);
                } catch(NumberFormatException e){
                    System.out.println("> " + entrada + " nao eh um numero");
                    continue;
                }

                try{
                    factors=server.fatorar(numero);
                    
                    System.out.println("> fatoracao em primos de " + numero);
                    System.out.print("> [");

                    for(int i=0;i<factors.size();i++){
                        System.out.print(factors.get(i));

                        if(i<factors.size()-1){
                            System.out.print(", ");
                        }
                    }

                    System.out.println("]"); 
                } catch(RemoteException e){
                    System.out.println("> erro: " + e.getMessage());
                }
            }
            scanner.close();

        } catch(Exception e){
            e.printStackTrace();
        }
    }
}