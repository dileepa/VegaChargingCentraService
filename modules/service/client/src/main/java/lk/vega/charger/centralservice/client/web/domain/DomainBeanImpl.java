package lk.vega.charger.centralservice.client.web.domain;

import lk.vega.charger.centralservice.client.web.domain.chargeStation.ChargeStationBean;
import lk.vega.charger.centralservice.client.web.domain.location.LocationBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Intelij Idea IDE
 * Created by dileepa.
 * Date on 4/24/15.
 * Time on 1:28 PM
 */
public class DomainBeanImpl implements DomainBean
{
    /**
     *
     * This is the Abstract Class for create bean object.
     * in Child class must contain
     * override below methods
     * getters and setters
     */

    public static final int LOCATION_BEAN_ID = 1;
    public static final int CHARGE_STATION_BEAN_ID = 2;


    @Override
    public void createBean( Object object )
    {

    }

    @Override
    public void decodeBeanToReal( Object object )
    {

    }

    public static List<DomainBean> getBeanList( List objectList, int identifier )
    {
        Object beanObject = null;
        switch( identifier )
        {
            case 1:
                beanObject = new LocationBean();
                break;
            case 2:
                beanObject = new ChargeStationBean();
                break;
            default:
                break;
        }
        List<DomainBean> locationBeanList = new ArrayList<DomainBean>();
        Class dynamicClass = beanObject.getClass();
        for( Object o : objectList )
        {
            try
            {
                Object bean = dynamicClass.newInstance();
                DomainBean domainBean = (DomainBean)bean;
                domainBean.createBean( o );
                locationBeanList.add( domainBean );
            }
            catch( InstantiationException e )
            {
                e.printStackTrace();
            }
            catch( IllegalAccessException e )
            {
                e.printStackTrace();
            }
            catch( Exception e )
            {
                e.printStackTrace();
            }
        }
        return locationBeanList;
    }
}
