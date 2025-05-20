package service;
import dataaccess.*;
import model.*;

public class Service {
    public static void clear(){
        new MemoryAuthDAO().clear();
        new MemoryGameDAO().clear();
        new MemoryUserDAO().clear();
    }

    //other 6

}
