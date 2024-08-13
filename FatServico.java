import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface FatServico extends Remote{
    List<Long> fatorar(long number) throws RemoteException;
}