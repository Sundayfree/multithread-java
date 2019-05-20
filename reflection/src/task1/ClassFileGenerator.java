package task1;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.Arrays;

import task1.CompactDisc;

public class ClassFileGenerator {
	private Class clazz = CompactDisc.class;
	private StringBuilder builder = new StringBuilder();

	public static void main(String[] args) {
	
		String classInfo = new ClassFileGenerator().getClassInfo();
		System.out.println(classInfo);
		printPrettyJavaFile(classInfo);
		System.out.println();

	}
	 private static void printPrettyJavaFile(String classInfo) {
		
		  	String PATH=System.getProperty("user.dir")+"\\src\\files\\";
	        BufferedWriter writer=null;
	        try{
	        	File file=new File (PATH+"CompactDisc.java");
	        	if(!file.exists()) file.createNewFile();
	        writer= new BufferedWriter (new FileWriter (file));
	            writer.write (classInfo);
	            writer.flush ();
	            System.out.println ("CompactDisc.java created..........");

	        }catch(Exception e){
	            e.printStackTrace ();
	        }finally {
	            if(writer !=null){
	                try{
	                    writer.close ();
	                }catch(Exception e){
	                   e.printStackTrace ();
	            }
	        }
	    }
	}

	public String getClassInfo() {

		return builder.toString();
	}

	public ClassFileGenerator() {
		super();
		appendClassAndInterface();
		appendFields();
		appendConstructors();
		appendMethods();
	}

	private void appendClassAndInterface() {
        builder.append("package "+clazz.getPackage().getName()+";\n\n").
				append(getModifier(clazz.getModifiers()) + " ").append(clazz.getSimpleName() + " extends ")
				.append(clazz.getSuperclass().getSimpleName() + " implements ")
				.append(clazz.getInterfaces()[0].getSimpleName() + "{\n");
	}

	private void appendFields() {
		Arrays.asList(clazz.getDeclaredFields()).forEach(field -> {
			field.setAccessible(true);
			int num = field.getModifiers();
			String modifier = getModifier(num);
			String type = field.getType().getSimpleName();
			String name = field.getName();
			if (num == (Modifier.PRIVATE + Modifier.FINAL)) {
				if (field.getType() == String.class)
					builder.append(
							"    " + modifier + " " + type + " " + name + " = \" " + getFieldValue(field) + "\";\n");
				else
					builder.append(
							"    " + modifier + " " + type + " " + name + " = \" " + getFieldValue(field) + "\";\n");
			} else {
				builder.append("    " + modifier + " " + type + " " + name + ";\n");
			}
		});

	}

	private void appendConstructors() {
		Arrays.asList(clazz.getConstructors()).forEach(con -> {
			String name = getSimpleName(con.getName());
			String modifier = getModifier(con.getModifiers());
			Parameter[] parameters = con.getParameters();
			if (parameters.length == 0)
				builder.append("    " + modifier + " " + name + "(){}\n");
			else {
				builder.append("    " + modifier + " " + name + "(");
				int count = 0;
				for (Parameter p : parameters) {
					if (count++ == parameters.length - 1) {
						builder.append(p.getType().getSimpleName() + " " + p.getName() + "){}\n");
					} else
						builder.append(p.getType().getSimpleName() + " " + p.getName() + ", ");
				}
			}

		});
	}

	private void appendMethods() {
		Arrays.asList(clazz.getDeclaredMethods()).forEach(method->{
			method.setAccessible(true);
			String name = method.getName();
			builder.append("    " + getModifier(method.getModifiers()) + " ")
					.append(method.getReturnType().getSimpleName() + " ");
			if (name.indexOf("set") == 0) {
				Arrays.asList(method.getParameters()).forEach(p -> {
					builder.append(method.getName() + "(" + p.getType().getSimpleName() + " " + p.getName() + "){}\n");
				});
			} else
				builder.append(method.getName() + "(){}\n");
		
		}); 
		builder.append("}");
	}

	private static String getSimpleName(String name) {
		return name.substring(name.lastIndexOf(".") + 1);
	}

	private String getFieldValue(Field f) {
		try {
			CompactDisc instance = (CompactDisc) clazz.getConstructor(null).newInstance(null);
			return f.get(instance).toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private String getModifier(int modifiers) {

		switch (modifiers) {
		case Modifier.FINAL + Modifier.PRIVATE:
			return "private final";
		case Modifier.PRIVATE:
			return "private";
		case Modifier.PRIVATE + Modifier.STATIC + Modifier.FINAL:
			return "public static final";
		case Modifier.PUBLIC:
			return "public";
		}
		return "public";
	}

}
