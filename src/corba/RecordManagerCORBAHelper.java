package corba;


/**
* corba/RecordManagerCORBAHelper.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from D:/Progs/Java-Progs/comp6231/src/corba/RecordManagerCORBA.idl
* Saturday, June 9, 2018 10:43:39 PM EDT
*/

abstract public class RecordManagerCORBAHelper
{
  private static String  _id = "IDL:corba/RecordManagerCORBA:1.0";

  public static void insert (org.omg.CORBA.Any a, corba.RecordManagerCORBA that)
  {
    org.omg.CORBA.portable.OutputStream out = a.create_output_stream ();
    a.type (type ());
    write (out, that);
    a.read_value (out.create_input_stream (), type ());
  }

  public static corba.RecordManagerCORBA extract (org.omg.CORBA.Any a)
  {
    return read (a.create_input_stream ());
  }

  private static org.omg.CORBA.TypeCode __typeCode = null;
  synchronized public static org.omg.CORBA.TypeCode type ()
  {
    if (__typeCode == null)
    {
      __typeCode = org.omg.CORBA.ORB.init ().create_interface_tc (corba.RecordManagerCORBAHelper.id (), "RecordManagerCORBA");
    }
    return __typeCode;
  }

  public static String id ()
  {
    return _id;
  }

  public static corba.RecordManagerCORBA read (org.omg.CORBA.portable.InputStream istream)
  {
    return narrow (istream.read_Object (_RecordManagerCORBAStub.class));
  }

  public static void write (org.omg.CORBA.portable.OutputStream ostream, corba.RecordManagerCORBA value)
  {
    ostream.write_Object ((org.omg.CORBA.Object) value);
  }

  public static corba.RecordManagerCORBA narrow (org.omg.CORBA.Object obj)
  {
    if (obj == null)
      return null;
    else if (obj instanceof corba.RecordManagerCORBA)
      return (corba.RecordManagerCORBA)obj;
    else if (!obj._is_a (id ()))
      throw new org.omg.CORBA.BAD_PARAM ();
    else
    {
      org.omg.CORBA.portable.Delegate delegate = ((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate ();
      corba._RecordManagerCORBAStub stub = new corba._RecordManagerCORBAStub ();
      stub._set_delegate(delegate);
      return stub;
    }
  }

  public static corba.RecordManagerCORBA unchecked_narrow (org.omg.CORBA.Object obj)
  {
    if (obj == null)
      return null;
    else if (obj instanceof corba.RecordManagerCORBA)
      return (corba.RecordManagerCORBA)obj;
    else
    {
      org.omg.CORBA.portable.Delegate delegate = ((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate ();
      corba._RecordManagerCORBAStub stub = new corba._RecordManagerCORBAStub ();
      stub._set_delegate(delegate);
      return stub;
    }
  }

}
