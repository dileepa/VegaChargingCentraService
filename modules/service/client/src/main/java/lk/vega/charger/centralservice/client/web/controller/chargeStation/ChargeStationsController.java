package lk.vega.charger.centralservice.client.web.controller.chargeStation;

import lk.vega.charger.centralservice.client.web.dataLoader.chargeStation.ChargeStationLoader;
import lk.vega.charger.centralservice.client.web.dataLoader.chargeStation.ChargeStationStatusLoader;
import lk.vega.charger.centralservice.client.web.dataLoader.loacation.LocationLoader;
import lk.vega.charger.centralservice.client.web.domain.DomainBeanImpl;
import lk.vega.charger.centralservice.client.web.domain.chargeStation.ChargeStationBean;
import lk.vega.charger.centralservice.client.web.domain.chargeStation.ChargeStationStatusBean;
import lk.vega.charger.centralservice.client.web.domain.location.LocationBean;
import lk.vega.charger.centralservice.client.web.domain.user.User;
import lk.vega.charger.core.ChargeLocation;
import lk.vega.charger.core.ChargePoint;
import lk.vega.charger.util.ChgResponse;
import lk.vega.charger.util.CoreController;
import lk.vega.charger.util.Savable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * Intelij Idea IDE
 * Created by dileepa.
 * Date on 4/24/15.
 * Time on 1:12 PM
 */
@Controller
public class ChargeStationsController
{
    @RequestMapping(value = "/AllChargeStations", method = RequestMethod.GET)
    public ModelAndView index()
    {
        ChgResponse chgResponse = ChargeStationLoader.loadAllChargePoints();
        ModelAndView modelAndView = new ModelAndView();
        if (chgResponse.isSuccess())
        {
            List<ChargePoint> allChargePointList = (List)chgResponse.getReturnData();
            List allChargePointBeanList = ChargeStationBean.getBeanList( allChargePointList, DomainBeanImpl.CHARGE_STATION_BEAN_ID );
            modelAndView.setViewName( "chargeStation/chargeStations" );
            modelAndView.getModel().put( "chgStations", allChargePointBeanList );
        }
        return modelAndView;
    }

    @RequestMapping(value = "/chargeStation/editChargeStation", method = RequestMethod.GET)
    public ModelAndView editLocation(@RequestParam(value = "chgStationID", required = false ) Integer chgStationID )
    {
        ChgResponse chgResponse = ChargeStationLoader.loadSpecificChargePointByPointID(chgStationID);
        ModelAndView modelAndView = new ModelAndView();
        if (chgResponse.isSuccess())
        {
            ChargePoint chargePoint = (ChargePoint)chgResponse.getReturnData();
            ChargeStationBean chargeStationBean = new ChargeStationBean();
            chargeStationBean.createBean( chargePoint );
            modelAndView.setViewName( "chargeStation/editChargeStation" );
            modelAndView.getModel().put( "chargeStation", chargeStationBean );
            ChgResponse chgLocationsResponse = LocationLoader.loadAllChargeLocations();
            if (chgLocationsResponse.isSuccess())
            {
                List<ChargeLocation> chargeLocations = (List)chgLocationsResponse.getReturnData();
                List locationBeanList = LocationBean.getBeanList( chargeLocations, DomainBeanImpl.LOCATION_BEAN_ID );
                for( LocationBean locationBean : (List<LocationBean>) locationBeanList )
                {
                    locationBean.setSelected( chargeStationBean.getLocationId() == locationBean.getLocationId() );
                }
                modelAndView.getModel().put( "locations", locationBeanList );
            }
            ChgResponse chgPointStatusRes = ChargeStationStatusLoader.loadAllChargePointStatus();
            if( chgPointStatusRes.isSuccess() )
            {
                List<String> chargePointStatus = (List) chgPointStatusRes.getReturnData();
                List statusBeanList = ChargeStationStatusBean.getBeanList( chargePointStatus, DomainBeanImpl.CHARGE_STATION_STATUS_BEAN_ID );
                for( ChargeStationStatusBean status : (List<ChargeStationStatusBean>)statusBeanList )
                {
                    status.setSelected( status.getName().equals( chargeStationBean.getChargePointStatus() ) );
                }
                modelAndView.getModel().put( "statusList",statusBeanList );
            }
        } return modelAndView;
    }

    @RequestMapping(value = "/saveExistingChargeStation", method = RequestMethod.POST)
    public ModelAndView updateChargeStation(SecurityContextHolderAwareRequestWrapper request,@ModelAttribute("chargeStation" ) ChargeStationBean chargeStationBean )
    {
        User loggedUser = ( User)( (UsernamePasswordAuthenticationToken) request.getUserPrincipal() ).getPrincipal();
        ChargePoint chargePoint = new ChargePoint();
        chargePoint.init();
        chargeStationBean.decodeBeanToReal( chargePoint );
        chargePoint.setUserName( loggedUser.getUsername() );
        chargePoint.setStatus( Savable.MODIFIED );
        ChgResponse chgResponse = CoreController.save( chargePoint );
        ModelAndView modelAndView = new ModelAndView();
        if (chgResponse.isSuccess())
        {
            ChgResponse loadedChargePointResponse = ChargeStationLoader.loadSpecificChargePointByPointID( chargePoint.getChargePointId() );
            if (loadedChargePointResponse.isSuccess())
            {
                ChargePoint updatedChargePoint = (ChargePoint)loadedChargePointResponse.getReturnData();
                ChargeStationBean updateChargeStationBean = new ChargeStationBean();
                updateChargeStationBean.createBean( updatedChargePoint );
                modelAndView.setViewName( "chargeStation/loadChargeStation" );
                modelAndView.getModel().put( "chargeStation", updateChargeStationBean );
            }
        }
        return modelAndView;
    }

    @RequestMapping(value = "/saveNewChargeStation", method = RequestMethod.POST)
    public ModelAndView saveNewLocation(SecurityContextHolderAwareRequestWrapper request,@ModelAttribute("chargeStation" ) ChargeStationBean chargeStationBean )
    {
//        chargeStationBean.setUserId( 1 ); //TODO remove user id logic after user login control
        User loggedUser = ( User)( (UsernamePasswordAuthenticationToken) request.getUserPrincipal() ).getPrincipal();
        ChargePoint chargePoint = new ChargePoint();
        chargeStationBean.decodeBeanToReal( chargePoint );
        chargePoint.setStatus( Savable.NEW );
        chargePoint.setUserName( loggedUser.getUsername() );
        ChgResponse chgResponse = CoreController.save( chargePoint );
        ModelAndView modelAndView = new ModelAndView();
        if (chgResponse.isSuccess())
        {
            ChgResponse loadedChargePointResponse = ChargeStationLoader.loadSpecificChargePointByReference( chargePoint.getReference() );
            if (loadedChargePointResponse.isSuccess())
            {
                ChargePoint updatedChargePoint = (ChargePoint)loadedChargePointResponse.getReturnData();
                ChargeStationBean updateChargeStationBean = new ChargeStationBean();
                updateChargeStationBean.createBean( updatedChargePoint );
                modelAndView.setViewName( "chargeStation/loadChargeStation" );
                modelAndView.getModel().put( "chargeStation", updateChargeStationBean );
            }
        }
        return modelAndView;
    }

    @RequestMapping(value = "/chargeStation/addChargeStation", method = RequestMethod.GET)
    public ModelAndView addLocation()
    {
        ModelAndView modelAndView = new ModelAndView();
        ChargeStationBean chargeStationBean = new ChargeStationBean();
        ChargePoint chargePoint = new ChargePoint();
        chargePoint.init();
        chargeStationBean.createBean( chargePoint );
        modelAndView.setViewName( "chargeStation/addChargeStation" );
        modelAndView.getModel().put( "chargeStation", chargeStationBean );
        ChgResponse chgLocationsResponse = LocationLoader.loadAllChargeLocations();
        if (chgLocationsResponse.isSuccess())
        {
            List<ChargeLocation> chargeLocations = (List)chgLocationsResponse.getReturnData();
            List locationBeanList = LocationBean.getBeanList( chargeLocations, DomainBeanImpl.LOCATION_BEAN_ID );
            modelAndView.getModel().put( "locations", locationBeanList );
        }
        ChgResponse chgPointStatusRes = ChargeStationStatusLoader.loadAllChargePointStatus();
        if( chgPointStatusRes.isSuccess() )
        {
            List<String> chargePointStatus = (List) chgPointStatusRes.getReturnData();
            List statusBeanList = ChargeStationStatusBean.getBeanList( chargePointStatus, DomainBeanImpl.CHARGE_STATION_STATUS_BEAN_ID );
            for( ChargeStationStatusBean status : (List<ChargeStationStatusBean>)statusBeanList )
            {
                status.setSelected( status.getName().equals( chargeStationBean.getChargePointStatus() ) );
            }
            modelAndView.getModel().put( "statusList",statusBeanList );
        }
        return modelAndView;
    }
}
