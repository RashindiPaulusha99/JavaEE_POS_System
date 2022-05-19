package BO;

public class BOFactory {
    private static BOFactory boFactory;

    private BOFactory(){

    }

    public static BOFactory getBoFactory(){
        if (boFactory == null){
            boFactory = new BOFactory();
        }
        return boFactory;
    }

    public enum BOTypes{
        CUSTOMER,ITEM,ORDER,ORDERDETAIL
    }

    public SuperBO getBO(BOTypes types){
        switch (types){
            case CUSTOMER:
                return new CustomerBOImpl();
            case ITEM:
                //return new ItemBOImpl();
            case ORDER:
                //return new OrderBOImpl();
            case ORDERDETAIL:
                //return new OrderDetailBOImpl();
            default:
                return null;
        }
    }
}
