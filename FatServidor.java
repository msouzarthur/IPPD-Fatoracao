import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class FatServidor extends UnicastRemoteObject implements FatServico{

    private final ExecutorService threadPool;

    protected FatServidor(int numThreads) throws RemoteException{
        super();
        this.threadPool = Executors.newFixedThreadPool(numThreads);

        System.out.println("> inciando");
        System.out.println("> usando " + numThreads + " threads");
    }

    @Override
    public List<Long> fatorar(long numero) throws RemoteException{
        System.out.println("> fatorando " + numero);

        Future<List<Long>> future = threadPool.submit(() -> fatores(numero));

        try{
            return future.get();
        } catch (Exception e){
            throw new RemoteException("> erro: ", e);
        }
    }

    private List<Long> fatores(long numero){
        List<Long> factors = new ArrayList<>();

        if(numero<2){
            factors.add(numero);
        } else{
            for(long i=2;i<=numero/i;i++){
                while(numero%i==0){
                    factors.add(i);
                    numero/=i;
                }
            }

            if(numero>1){
                factors.add(numero); 
            }
        }
        return factors;
    }

    public static void main(String[] args){
        try{
            int numThreads = 4;

            if(args.length>0){
                try{
                    numThreads = Integer.parseInt(args[0]);

                    if(numThreads<=0){
                        System.out.println("> numero de threads invalido");
                        numThreads = 4;
                    }
                } catch(NumberFormatException e){
                    System.out.println("> entrada invalida");
                }
            
            }            
            FatServidor server = new FatServidor(numThreads);
            Registry registry = LocateRegistry.createRegistry(1099);
            
            registry.rebind("FatServico", server);
            System.out.println("> acordando");
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
