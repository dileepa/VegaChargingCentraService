package lk.vega.charger.util;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.Calendar;


/**
 * User: ruwan - copy from codegen
 * VegaDate: Dec 26, 2005
 * Time: 4:16:14 PM
 * Desc: If you set date by each field separately, follow the order year >> month >> date.
 *  Other wise you may get different date as output.
 */
public class VegaTime implements Comparable, Cloneable, Serializable
{
    private static final long serialVersionUID = -2260496048779768521L;
    int hour;
    int minute;

    public VegaTime()
    {
        hour = 0;
        minute = 0;
    }

    public VegaTime( int hour, int minute )
    {
        this.hour = hour;
        this.minute = minute;
    }

    public VegaTime( int time )
    {
        this.hour = time / 100;
        this.minute = time % 100;
    }

    public VegaTime( java.sql.Time time )
    {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis( time.getTime() );
        hour = cal.get( Calendar.HOUR_OF_DAY );
        minute = cal.get( Calendar.MINUTE );
    }


    public int getHour()
    {
        return hour;
    }

    public void setHour( int hour )
    {
        this.hour = hour;
    }

    public int getMinute()
    {
        return minute;
    }

    public void setMinute( int minute )
    {
        this.minute = minute;
    }

    public Object clone()
    {
        return new VegaTime( getHour(), getMinute() );
    }

    public int compareTo( Object o )
    {
        if( equals( (VegaTime) o ) )
        {
            return 0;
        }
        else if( after( (VegaTime) o ) )
        {
            return 1;
        }
        else
        {
            return 0;
        }
    }

    public boolean equals( VegaTime VegaTime )
    {
        return hour == VegaTime.getHour() &&
                minute == VegaTime.getMinute();
    }

    public boolean after( VegaTime time )
    {
        if( hour > time.getHour() )
        {
            return true;
        }
        else if( hour < time.getHour() )
        {
            return false;
        }
        else if( minute > time.getMinute() ) //hours are equal
        {
            return true;
        }
        else if( minute < time.getMinute() )//hours are equal
        {
            return false;
        }

        // is equal
        return false;
    }

    public boolean before( VegaTime time )
    {
        if( hour > time.getHour() )
        {
            return false;
        }
        else if( hour < time.getHour() )
        {
            return true;
        }
        else if( minute > time.getMinute() ) //hours are equal
        {
            return false;
        }
        else if( minute < time.getMinute() )//hours are equal
        {
            return true;
        }

        // is equal
        return false;
    }

    public String toString()
    {
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMinimumIntegerDigits( 2 );
        nf.setMaximumIntegerDigits( 2 );
        nf.setMinimumFractionDigits( 0 );
        nf.setMaximumFractionDigits( 0 );

        return nf.format( hour ) + ":" + nf.format( minute );
    }


    public int _getIntegerValue()
    {
        return hour * 100 + minute;
    }

    public long _getTime()
    {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis( 0 );
        cal.add( hour, Calendar.HOUR_OF_DAY );
        cal.add( minute, Calendar.MINUTE );
        return cal.getTimeInMillis();
    }


}
