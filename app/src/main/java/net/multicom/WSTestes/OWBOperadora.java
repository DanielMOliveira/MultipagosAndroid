package net.multicom.WSTestes;

//----------------------------------------------------
//
// Generated by www.easywsdl.com
// Version: 4.1.9.1
//
// Created by Quasar Development at 08-12-2015
//
//---------------------------------------------------


import java.util.Hashtable;
import org.ksoap2.serialization.*;

public class OWBOperadora extends AttributeContainer implements KvmSerializable
{

    
    public String _x005F_x003C_Codigo_x005F_x003E_k__BackingField;
    
    public String _x005F_x003C_Nome_x005F_x003E_k__BackingField;

    public OWBOperadora ()
    {
    }

    public OWBOperadora (java.lang.Object paramObj,OWBExtendedSoapSerializationEnvelope __envelope)
    {
	    
	    if (paramObj == null)
            return;
        AttributeContainer inObj=(AttributeContainer)paramObj;


        if(inObj instanceof SoapObject)
        {
            SoapObject soapObject=(SoapObject)inObj;
            int size = soapObject.getPropertyCount();
            for (int i0=0;i0< size;i0++)
            {
                //if you have compilation error here, please use a ksoap2.jar and ExKsoap2.jar from libs folder (in the generated zip file)
                PropertyInfo info= soapObject.getPropertyInfo(i0);
                java.lang.Object obj = info.getValue(); 
                if (info.name.equals("_x003C_Codigo_x003E_k__BackingField"))
                {
        
                    if (obj != null && obj.getClass().equals(SoapPrimitive.class))
                    {
                        SoapPrimitive j =(SoapPrimitive) obj;
                        if(j.toString()!=null)
                        {
                            this._x005F_x003C_Codigo_x005F_x003E_k__BackingField = j.toString();
                        }
                    }
                    else if (obj!= null && obj instanceof String){
                        this._x005F_x003C_Codigo_x005F_x003E_k__BackingField = (String)obj;
                    }
                    continue;
                }
                if (info.name.equals("_x003C_Nome_x003E_k__BackingField"))
                {
        
                    if (obj != null && obj.getClass().equals(SoapPrimitive.class))
                    {
                        SoapPrimitive j =(SoapPrimitive) obj;
                        if(j.toString()!=null)
                        {
                            this._x005F_x003C_Nome_x005F_x003E_k__BackingField = j.toString();
                        }
                    }
                    else if (obj!= null && obj instanceof String){
                        this._x005F_x003C_Nome_x005F_x003E_k__BackingField = (String)obj;
                    }
                    continue;
                }

            }

        }



    }

    @Override
    public java.lang.Object getProperty(int propertyIndex) {
        //!!!!! If you have a compilation error here then you are using old version of ksoap2 library. Please upgrade to the latest version.
        //!!!!! You can find a correct version in Lib folder from generated zip file!!!!!
        if(propertyIndex==0)
        {
            return this._x005F_x003C_Codigo_x005F_x003E_k__BackingField!=null?this._x005F_x003C_Codigo_x005F_x003E_k__BackingField:SoapPrimitive.NullNilElement;
        }
        if(propertyIndex==1)
        {
            return this._x005F_x003C_Nome_x005F_x003E_k__BackingField!=null?this._x005F_x003C_Nome_x005F_x003E_k__BackingField:SoapPrimitive.NullNilElement;
        }
        return null;
    }


    @Override
    public int getPropertyCount() {
        return 2;
    }

    @Override
    public void getPropertyInfo(int propertyIndex, @SuppressWarnings("rawtypes") Hashtable arg1, PropertyInfo info)
    {
        if(propertyIndex==0)
        {
            info.type = PropertyInfo.STRING_CLASS;
            info.name = "_x003C_Codigo_x003E_k__BackingField";
            info.namespace= "http://schemas.datacontract.org/2004/07/MulticomNet.MultiPagos.Domain.Recarga";
        }
        if(propertyIndex==1)
        {
            info.type = PropertyInfo.STRING_CLASS;
            info.name = "_x003C_Nome_x003E_k__BackingField";
            info.namespace= "http://schemas.datacontract.org/2004/07/MulticomNet.MultiPagos.Domain.Recarga";
        }
    }



    @Override
    public void setProperty(int arg0, java.lang.Object arg1)
    {
    }

}
