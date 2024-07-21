### 类加载

如果我是javac的设计者，我会怎么设计将java文件转为class文件，让jvm容易识别?

根据编译原理书本，进行词法分析，语法分析，判定是否符合java语法，生成语法树，通过语义解析，用到字节码生成器去生成最终我们想要生成的文件。

![image-20240601093042584](https://gitee.com/zhf19970510/image-server/raw/master/img_2024/20240601093050.png)

常量池主要存储了：

字面量：文本、字符串、final修饰的内容。

符号引用：类、接口、字段、方法、明细信息相关的描述

反编译：

javap -p -v Person.class > a.txt

类加载机制应该做的事情：

![image-20240601172916974](https://gitee.com/zhf19970510/image-server/raw/master/img_2024/20240601172924.png)

#### 类加载的流程：

![image-20240601182342573](C:\Users\admin\AppData\Roaming\Typora\typora-user-images\image-20240601182342573.png)

所谓类加载机制就是：

虚拟机把class文件加载到内存，并对数据进行校验、转换解析和初始化，形成虚拟机可以直接使用的Java类型，即java.lang.Class。

  javap -v -p Person.class 进行反编译，查看字节码信息和指令等信息。



装载的设计者：

class文件 ---> 字节流 ---> 寻找器（类加载器）		自定义类加载器，字节码增强操作。java Agent

类加载器独立于jvm之外，它可以通过类的全限定名找到这个类的二进制字节流。

将这个字节流所代表的静态存储结构转换成方法区的运行时数据结构。

在java堆中生成一个代表这个类的java.lang.Class对象，做为方法区的数据访问入口。

验证：

验证class文件中的各种信息要符合虚拟机的要求，并且要求对应的字节流信息不会危害虚拟机自身的安全，。

文件格式验证

如是否以cafebabe开头

元数据验证

是对于java语法的验证

字节码验证：

数据流以及控制流的分析

1. 运行时检查
2. 栈数据类型和操作码

符号引用验证：

符号引用转变为直接引用

取消验证方法：

-Xverify:none



准备：

为类的静态变量分配内存，并且设置该类型的默认值。（不包括final 修饰的static，final 的 static会在编译器分配内存）

进行分配内存的只是包含类变量（静态变量），而不包括实例变量，实例变量是在对象实例化时随着对象一起分配在java堆中的。通常情况下初始值为对应类型默认值，假设public int a = 1; 那么a在准备阶段过后的初始值为0，不为1，这时候只是开辟了内存空间，并没有运行java代码，a赋值1的指令是在程序被编译后，存放于构造器()方法之中，所以a赋值为1是在初始化阶段才会执行。



解析：

解析是从运行时常量池中的符号引用动态确定具体值的过程。

符号引用转变为直接引用

对解析的结果进行缓存。

invokeddynamic指令	应用于java8 lambda表达式当中



初始化：

执行类构造器方法的过程		Clinit方法

1. 声明类变量为指定的初始值
2. 使用静态代码块为类变量赋值

JVM初始化步骤：

假设这个类还没有被加载和连接，则程序先加载并连接该类

假设该类的直接父类还没有被初始化，则先初始化其直接父类

假设类中有初始化语句，则系统依次执行这些初始化语句



类的初始化什么时候会被触发？

主动引用：

​	只有当对类的主动使用的时候才会导致类的初始化，类的主动初始化有六种：

- 创建类的实例，也就是new的方式
- 访问某个类或者接口的静态变量，或者对该静态变量赋值
- 调用类的静态方法
- 反射（如Class.forName("com.carl.Test");
- 初始化某个类的子类，则其父类也会被初始化。
- java虚拟机启动时被标明为启动类的类（JvmCaseApplication），直接使用java.exe命令来运行某个主类

被动引用：

- 引用父类的静态字段，只会引起父类的初始化，而不会引起子类的初始化
- 定义类数组，不会引起类的初始化。
- 引用类的static final常量，不会引起类的初始化（如果只有static修饰，还是会引起该类初始化的）。



类卸载：

在类使用完之后，如果满足下面的情况，类就会被卸载：

该类所有实例都已经被回收，也就是java堆中不存在该类的任何实例。

加载该类的ClassLoader已经被回收

该类对应的java.lang.Class对象没有任何地方被引用，无法在任何地方通过反射访问该类的方法。





![image-20240601202016197](https://gitee.com/zhf19970510/image-server/raw/master/img_2024/20240601202016.png)

jvm性能调优、mysql性能调优、



类加载器、运行时数据区、常量池、栈帧



对象的内存布局



### 类加载器

#### 什么是类加载器？

负责读取java字节代码，并转换成java.lang.Class类的一个实例的代码模块。

类加载器除了用于加载类外，还可用于确定类在Java虚拟机中的唯一性。

一个类在同一个类加载器中具有唯一性，而不同类加载器中是允许同名类存在的，这里的同名是指全限定名相同。但是在整个JVM里，纵然全限定名相同，若类加载器不同，则仍然不算作是同一个类，无法通过instanceOf、equals等方式的校验。



类加载器分类：

1）Bootstrap ClassLoader 负责加载$JAVA_HOME中 jre/lib/rt.jar 里所有的class或Xbootclassoath选项指定的jar包。由C++实现，不是ClassLoader子类。

2）Extension ClassLoader 负责加载java平台中扩展功能的一些jar包，包括$JAVA_HOME中jre/lib/*.jar 或 -Djava.ext.dirs指定目录下的jar包。

3）App ClassLoader 负责加载classpath中指定的jar包及 Djava.class.path 所指定目录下的类和jar包。

4）Custom ClassLoader 通过 java.lang.ClassLoader的子类自定义加载class，属于应用程序根据自身需要自定义的classLoader，如tomcat、jboss都会根据j2ee规范自行实现ClassLoader。

![image-20240602100202959](https://gitee.com/zhf19970510/image-server/raw/master/img_2024/20240602100203.png)

JVM类加载器三大特性：

全盘负责：当一个类加载器加载某个Class时，该Class所依赖的和引用的其他Class也将由该类加载器负责载入，除非显示使用另一个类加载器来载入。

> 例如，系统类加载器AppClassLoader加载入口类（含main方法的类）时，会把main方法所依赖的类及引用的类也载入，依次类推。“全盘负责” 机制也可称为当前类加载器负责机制。显然，入口类所依赖的类及引用的类的当前类加载器就是入口类的类加载器。

父类委托：“双亲委派”是指子类加载器如果没有加载过该目标类，就先委托父类加载器加载该目标类，只有在父类加载器找不到字节码文件的情况下才从自己的类路径中查找并装载目标类。

缓存机制：缓存机制将会保证所有加载过的Class都将在内存中缓存，当程序中需要使用某个Class时，类加载器先从内存的缓存区寻找该Class，只有缓存区不存在，系统才读取该类对应的二进制缓存数据，并将其转换成Class对象，存入缓存区。这就是为什么修改了Class后，必须重启JVM。对于一个类加载器实例来说，相同全名的类只加载一次，即loadClass方法不会被重复调用。



  “双亲委派”机制加载Class的具体过程是： 

1. ClassLoader先判断该Class是否已加载，如果已加载，则返回Class对象；如果没有则委托 给父类加载器。 
2. 父类加载器判断是否加载过该Class，如果已加载，则返回Class对象；如果没有则委托给 祖父类加载器。 
3. 依此类推，直到始祖类加载器（引用类加载器）。 
4. 始祖类加载器判断是否加载过该Class，如果已加载，则返回Class对象；如果没有则尝试 从其对应的类路径下寻找class字节码文件并载入。如果载入成功，则返回Class对象；如果 载入失败，则委托给始祖类加载器的子类加载器。 
5. 始祖类加载器的子类加载器尝试从其对应的类路径下寻找class字节码文件并载入。如果载 入成功，则返回Class对象；如果载入失败，则委托给始祖类加载器的孙类加载器。 
6. 依此类推，直到源ClassLoader。
7. 源ClassLoader尝试从其对应的类路径下寻找class字节码文件并载入。如果载入成功，则 返回Class对象；如果载入失败，源ClassLoader不会再委托其子类加载器，而是抛出异常。



我们可以继承java.lang.ClassLoader类，实现自己的类加载器。如果想保持双亲委派模型，就 应 该重写findClass(name)方法；如果想破坏双亲委派模型，可以重写loadClass(name)方法。



#### 打破双亲委派

双亲委派这个模型并不是强制模型，而且会带来一些些的问题。就比如java.sql.Driver这个东西。 JDK只能提供一个规范接口，而不能提供实现。提供实现的是实际的数据库提供商。提供商的库总不 能放JDK目录里吧。

所以java想到了几种办法可以用来打破我们的双亲委派。

SPI（service Provider Interface） :比如Java从1.6搞出了SPI就是为了优雅的解决这类问题——JDK 提供接口，供应商提供服务。编程人员编码时面向接口编程，然后JDK能够自动找到合适的实现，岂 不是很爽？

Java 在核心类库中定义了许多接口，并且还给出了针对这些接口的调用逻辑，然而并未给出实 现。开发者要做的就是定制一个实现类，在 META-INF/services 中注册实现类信息，以供核心 类库使用。比如JDBC中的DriverManager

**OSGI:**比如我们的JAVA程序员更加追求程序的动态性，比如代码热部署，代码热替换。也就是就 是机器不用重启，只要部署上就能用。OSGi实现模块化热部署的关键则是它自定义的类加载器机制的实现。每 一个程序模块都有一个自己的类加载器，当需要更换一个程序模块时，就把程序模块连同类加载器一 起 换掉以实现代码的热替换。



硬件一致性协议：

MSI、MESI、MOSI、Synapse、Firely、DragonProtocol



### 常量池分类：

#### 1.静态常量池

静态常量池是相对于运行时常量池来说的，属于描述class文件结构的一部分

由**字面量**和**符号引用**组成，在类被加载后会将静态常量池加载到内存中也就是运行时常量池

**字面量** ：文本，字符串以及Final修饰的内容

**符号引用** ：类，接口，方法，字段等相关的描述信息。

#### 2.运行时常量池（存储在方法区中）

当静态常量池被加载到内存后就会变成运行时常量池。

> 也就是真正的把文件的内容落地到JVM内存了

#### 3.字符串常量池

**设计理念：**字符串作为最常用的数据类型，为减小内存的开销，专门为其开辟了一块内存区域（字符串常量池）用以存放。

JDK1.6及之前版本，字符串常量池是位于永久代（相当于现在的方法区）。

JDK1.7之后，字符串常量池位于Heap堆中



![image-20240602171740444](https://gitee.com/zhf19970510/image-server/raw/master/img_2024/20240602171740.png)

![image-20240602192522837](https://gitee.com/zhf19970510/image-server/raw/master/img_2024/20240602192522.png)

####  Method Area(方法区)

**（1）方法区是各个线程共享的内存区域，在虚拟机启动时创建**

```
The Java Virtual Machine has a method area that is shared among all Java Virtual Machine threads. 
The method area is created on virtual machine start-up. 
```

**（2）虽然Java虚拟机规范把方法区描述为堆的一个逻辑部分，但是它却又一个别名叫做Non-Heap(非堆)，目的是与Java堆区分开来**

```
Although the method area is logically part of the heap,......
```

**（3）用于存储已被虚拟机加载的类信息、常量、静态变量、即时编译器编译后的代码等数据**

```
It stores per-class structures such as the run-time constant pool, field and method data, and the code for methods and constructors, including the special methods (§2.9) used in class and instance initialization and interface initialization.
```

**（4）当方法区无法满足内存分配需求时，将抛出OutOfMemoryError异常**

```
If memory in the method area cannot be made available to satisfy an allocation request, the Java Virtual Machine throws an OutOfMemoryError.
```

#### Heap(堆)

**（1）Java堆是Java虚拟机所管理内存中最大的一块，在虚拟机启动时创建，被所有线程共享。**

**（2）Java对象实例以及数组都在堆上分配。**

#### Java Virtual Machine Stacks(虚拟机栈)

> **经过上面的分析，类加载机制的装载过程已经完成，后续的链接，初始化也会相应的生效。**
>
> **假如目前的阶段是初始化完成了，后续做啥呢？肯定是Use使用咯，不用的话这样折腾来折腾去有什么意义？那怎样才能被使用到？换句话说里面内容怎样才能被执行？比如通过主函数main调用其他方法，这种方式实际上是main线程执行之后调用的方法，即要想使用里面的各种内容，得要以线程为单位，执行相应的方法才行。****那一个线程执行的状态如何维护？一个线程可以执行多少个方法？这样的关系怎么维护呢？**

**（1）虚拟机栈是一个线程执行的区域，保存着一个线程中方法的调用状态。换句话说，一个Java线程的运行状态，由一个虚拟机栈来保存，所以虚拟机栈肯定是线程私有的，独有的，随着线程的创建而创建。**

```
Each Java Virtual Machine thread has a private Java Virtual Machine stack, created at the same time as the thread.
```

**（2）每一个被线程执行的方法，为该栈中的栈帧，即每个方法对应一个栈帧。**

**调用一个方法，就会向栈中压入一个栈帧；一个方法调用完成，就会把该栈帧从栈中弹出。**

**栈帧：每个栈帧对应一个被调用的方法，可以理解为一个方法的运行空间。**

**每个栈帧中包括局部变量表(Local Variables)、操作数栈(Operand Stack)、指向运行时常量池的引用(A reference to the run-time constant pool)、方法返回地址(Return Address)和附加信息。**

局部变量表:方法中定义的局部变量以及方法的参数存放在这张表中
局部变量表中的变量不可直接使用，如需要使用的话，必须通过相关指令将其加载至操作数栈中作为操作数使用。

操作数栈:以压栈和出栈的方式存储操作数的

动态链接:每个栈帧都包含一个指向运行时常量池中该栈帧所属方法的引用，持有这个引用是为了支持方法调用过程中的动态连接(Dynamic Linking)。将符号引用转换为具体的方法引用。

方法返回地址:当一个方法开始执行后,只有两种方式可以退出，一种是遇到方法返回的字节码指令；一种是遇见异常，并且这个异常没有在方法体内得到处理。

#### The pc Register(程序计数器)

> **我们都知道一个JVM进程中有多个线程在执行，而线程中的内容是否能够拥有执行权，是根据CPU调度来的。**
>
> **假如线程A正在执行到某个地方，突然失去了CPU的执行权，切换到线程B了，然后当线程A再获得CPU执行权的时候，怎么能继续执行呢？这就是需要在线程中维护一个变量，记录线程执行到的位置。**

**如果线程正在执行Java方法，则计数器记录的是正在执行的虚拟机字节码指令的地址；**

**如果正在执行的是Native方法，则这个计数器为空。**



**那如果在Java方法执行的时候调用native的方法呢？**

![image-20240602202222702](https://gitee.com/zhf19970510/image-server/raw/master/img_2024/20240602202222.png)

![image-20240602202336990](https://gitee.com/zhf19970510/image-server/raw/master/img_2024/20240602202337.png)

小端存储和大端存储：

![image-20240608213442536](https://gitee.com/zhf19970510/image-server/raw/master/img_2024/20240608213449.png)

**小端存储** :便于数据之间的类型转换，例如:long类型转换为int类型时，高地址部分的数据可以直接截掉。

**大端存储** :便于数据类型的符号判断，因为最低地址位数据即为符号位，可以直接判断数据的正负号。

句柄池访问：

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/1463/1649922126094/d57189bb02aa4488809eb602b1562793.png)

直接指针访问对象图解:

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/1463/1649922126094/e7f4267aee394abf9d7d99c52c1ee150.png)

**区别:**

**句柄池:**

使用句柄访问对象，会在堆中开辟一块内存作为句柄池，句柄中储存了对象实例数据(属性值结构体) 的内存地址，访问类型数据的内存地址(类信息，方法类型信息)，对象实例数据一般也在heap中开 辟，类型数据一般储存在方法区中。

**优点** :reference存储的是稳定的句柄地址，在对象被移动(垃圾收集时移动对象是非常普遍的行为) 时只会改变句柄中的实例数据指针，而reference本身不需要改变。

**缺点** :增加了一次指针定位的时间开销。

**直接访问:**

直接指针访问方式指reference中直接储存对象在heap中的内存地址，但对应的类型数据访问地址需要 在实例中存储。

**优点** :节省了一次指针定位的开销。

**缺点** :在对象被移动时(如进行GC后的内存重新排列)，reference本身需要被修改。



一个Java对象在内存中包括3个部分：对象头、实例数据和对齐填充。

![image-20240609073651696](https://gitee.com/zhf19970510/image-server/raw/master/img_2024/20240609073651.png)

使用jol打印java对象内存分布，发现String只占用4个字节，按照上图应该占用8个字节才对，为什么只占用了4个字节，因为指针压缩技术的存在，JDK1.6之后，64位操作系统默认开启了指针压缩，什么时候指针压缩会失效？堆内存超过32G指针压缩会失效。

![image-20240609073925685](https://gitee.com/zhf19970510/image-server/raw/master/img_2024/20240609073925.png)

如果关闭指针压缩，将会发现占用字节数变成8Byte。使用jvm参数：-XX:-UseCompressedOops 可以关闭指针压缩。

![image-20240609142818242](https://gitee.com/zhf19970510/image-server/raw/master/img_2024/20240609142818.png)

### 内存模型设计之–指针压缩

&#x3e; 指针压缩的目的：
&#x3e;
&#x3e; 1. 为了保证CPU普通对象指针(oop)缓存
&#x3e; 2. 为了减少GC的发生，因为指针不压缩是8字节，这样在64位操作系统的堆上其他资源空间就少了。
&#x3e;
&#x3e; 64位操作系统中 内存 **&#x3e; 4G** 默认开启指针压缩技术，内存**&#x3c; 4G**，默认是32位系统默认不开启。内存 **&#x3e; 32G** 指针压缩失效。所以我们通常在部署服务时，JVM内存不要超过32G，因为超过32G就无法开启 指针压缩了。
&#x3e;
&#x3e; 内存 &#x3e; 32G指针压缩失效的原因是：4G**8 = 32G
&#x3e;
&#x3e; 32位系统的CPU 最大支持2^32 = 4G ,如果是64位系统，最大支持 2^64， 但是对其填充是按照8字节进行填充，指针压缩可以理解为在32位系统在64位上面使用，因为32位系统的CPU寻址空间最大支持4G，对其填充***8 = 32G，这就是内存&#x3e;32G指针压缩失效的原因。
&#x3e;
&#x3e; 关闭指针压缩 : -XX:-UseCompressedOops

### 内存模型设计之–对齐填充

对齐填充的意义是 **提高CPU访问数据的效率** ，主要针对会存在**该实例对象数据跨内存地址区域存储**的情况。

例如：在没有对齐填充的情况下，内存地址存放情况如下:

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/1463/1649922126094/bc2b480aee764ef4b4157e1cfdc7dcea.png)

因为处理器只能0x00-0x07，0x08-0x0F这样读取数据，所以当我们想获取这个long型的数据时，处理 器必须要读两次内存，第一次(0x00-0x07)，第二次(0x08-0x0F)，然后将两次的结果才能获得真正的数值。

那么在有对齐填充的情况下，内存地址存放情况是这样的:

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/1463/1649922126094/2942966ff63844ecbeacb8433e57958e.png)

现在处理器只需要直接一次读取(0x08-0x0F)的内存地址就可以获得我们想要的数据了。

### 运行时数据区

**上面对运行时数据区描述了很多，其实重点存储数据的是堆和方法区(非堆)，所以内存的设计也着重从这两方面展开(注意这两块区域都是线程共享的)。**

**对于虚拟机栈，本地方法栈，程序计数器都是线程私有的。**

**可以这样理解，JVM运行时数据区是一种规范，而JVM内存模式是对该规范的实现**

![image-20240609164015888](https://gitee.com/zhf19970510/image-server/raw/master/img_2024/20240609164015.png)



GC的悲观策略：在某些情况下，有的对象能不达到分代年龄，直接进入老年代。

比如：相同年龄的所有对象大小总和大于S区其中一个区域的一半，年龄大于或者等于这个年龄的对象可以直接进入老年代。

内存担保机制：大对象直接进入老年代

![image-20240609213807919](https://gitee.com/zhf19970510/image-server/raw/master/img_2024/20240609213808.png)

![image-20240609214015465](https://gitee.com/zhf19970510/image-server/raw/master/img_2024/20240609214015.png)

### 什么时候会触发full gc？

1.之前每次晋升的对象的平均大小   &#x3e;  老年代的剩余空间          基于历史平均水平

2.young  GC之后      存活对象超过了老年代的剩余空间           基于下一次可能的剩余空间

3.Meta Space区域空间不足

4.System.gc（）；



JDK1.7之前      Perm  space    永久代   持久代       JVM自己的内存   线性整理    会增加垃圾回收的时间

JDK1.8    Meta Space    元空间   元数据区       直接内存   减少内存碎片        节省压缩时间

### 如何判定对象已死？

可以使用jvisualvm工具观测jvm内存布局实时情况。

设置java线程java栈帧大小：

-Xss128k

设置堆的大小：

-Xms20M -Xmx20M

-XX:MetaspaceSize=50M -XX:MaxMetaspaceSize=50M

CPU使用率很高的情况下，适当降低垃圾回收的频率。

### 对象的生命周期

![image-20240610080939466](https://gitee.com/zhf19970510/image-server/raw/master/img_2024/20240610080939.png)

**应用阶段**

（1）系统至少维护着对象的一个强引用（Strong Reference）

（2）所有对该对象的引用全部是强引用（除非我们显式地使用了：软引用（Soft Reference）、弱引用（Weak Reference）或虚引用（Phantom Reference））

> 引用的定义：
>
> 1.我们的数据类型必须是引用类型
>
> 2.我们这个类型的数据所存储的数据必须是另外一块内存的起始地址

1.**强引用**

JVM内存管理器从根引用集合（Root Set）出发遍寻堆中所有到达对象的路径。当到达某对象的任意路径都不含有引用对象时，对这个对象的引用就被称为强引用

2.软引用 - 比较适合做缓存。网页缓存或者图片缓存。

（使用场景（当你去处理占用内存较大的对象  并且生命周期比较长的，不是频繁使用的））

问题：软引用可能会降低应用的运行效率与性能。比如：软引用指向的对象如果初始化很耗时，或者这个对象在进行使用的时候被第三方施加了我们未知的操作。

软引用是用来描述一些还有用但是非必须的对象。对于软引用关联的对象，在系统将于发生内存溢出异常之前，将会把这些对象列进回收范围中进行二次回收。

3.弱引用

弱引用（Weak Reference）对象与软引用对象的最大不同就在于：GC在进行回收时，需要通过算法检查是否回收软引用对象，而对于Weak引用对象， GC总是进行回收。因此Weak引用对象会更容易、更快被GC回收

4.虚引用

也叫幽灵引用和幻影引用，为一个对象设置虚引用关联的唯一目的就是能在这个对象被回收时收到一**个系统通知。也就是说,如果一个对象被设置上了一个虚引用,实际上跟没有设置引用没有**任何的区别

**不可见阶段**

不可见阶段的对象在虚拟机的对象根引用集合中再也找不到直接或者间接的强引用，最常见的就是线程或者函数中的临时变量。程序不在持有对象的强引用。  （但是某些类的静态变量或者JNI是有可能持有的 ）

**不可达阶段**

指对象不再被任何强引用持有，GC发现该对象已经不可达。

#### 引用计数法

**对于某个对象而言，只要应用程序中持有该对象的引用，就说明该对象不是垃圾，如果一个对象没有任何指针对其引用，它就是垃圾。**

`弊端`:如果AB相互持有引用（循环引用），导致永远不能被回收。

**能作为GC Root:类加载器、Thread、虚拟机栈的本地变量表、static成员、常量引用、本地方法栈的变量等。GC Roots本质上一组活跃的引用**

**收集阶段（Collected）**

GC发现对象处于不可达阶段并且GC已经对该对象的内存空间重新分配做好准备，对象进程收集阶段。如果，该对象的finalize()函数被重写，则执行该函数。

不建议重写finalize方法：重写finalize方法可能会造成下面的问题：

> 1.会影响到JVM的对象以及分配回收速度
>
> 2.可能造成对象再次复活（诈尸）

**终结阶段（Finalized）**

对象的finalize()函数执行完成后，对象仍处于不可达状态，该对象进程终结阶段。

**对象内存空间重新分配阶段（Deallocaled）**

GC对该对象占用的内存空间进行回收或者再分配，该对象彻底消失。



## 垃圾回收算法

### 标记清除算法

第一步：标记（找出内存中需要回收的对象，并且把它们标记出来）

第二步：清除（清除掉被标记需要回收的对象，释放出对应的内存空间）

**缺点：**

**标记清除之后会产生大量不连续的内存碎片，空间碎片太多可能会导致以后在程序运行过程中需要分配较大对象时，无法找到足够的连续内存而不得不提前触发另一次垃圾收集动作。**

**(1)标记和清除两个过程都比较耗时，效率不高**

**(2)会产生大量不连续的内存碎片，空间碎片太多可能会导致以后在程序运行过程中需要分配较大对象时，无法找到足够的连续内存而不得不提前触发另一次垃圾收集动作。**

**标记清除算法的衍生规则之分配（动态分区分配策略）**

**首次适应算法（Fisrt-fit）**

首次适应算法（Fisrt-fit）就是在遍历空闲链表的时候，一旦发现有大小等于需要的大小之后，就立即把该块分配给对象，并立即返回。

**最佳适应算法（Best-fit）**

最佳适应算法（Best-fit）就是在遍历空闲链表的时候，返回刚好等于需要大小的块。

**最差适应算法（Worst-fit）**

最差适应算法（Worst-fit）就是在遍历空闲链表的时候，找出空闲链表中最大的分块，将其分割给申请的对象，其目的就是使得分割后分块的最大化，以便下次好分配，不过这种分配算法很容易产生很多很小的分块，这些分块也不能被使用

### 复制算法

**将内存划分为两块相等的区域，每次只使用其中一块。**

**当其中一块内存使用完了，就将还存活的对象复制到另外一块上面，然后把已经使用过的内存空间一次清除掉**。

**缺点：**

**空间利用率降低**

### 标记-整理算法

**标记整理算法严格意义应该叫做标记清除整理算法或者标记清除压缩算法**

**因为他的本质就是在标记清除的基础在进行再整理**

### 常见整理算法

**随机整理**

随机整理：对象的移动方式和它们初始的对象排列及引用关系无关.

随机整理算法总结：

任意顺序整理实现简单，且执行速度快，但任意顺序可能会将原本相邻的对象打乱到不同的高速缓存行或者是虚拟内存页中（理解为打乱到内存各个地方），会降低赋值器的局部性。 包括他只能处理固定大小的对象，一旦对象大小不固定，就会增加其他的逻辑。

**线性整理**

线性顺序：将具有关联关系的对象排列在一起

相关的对象会进行整理，整理成一块块小区域，无法避免内存碎片

**滑动整理**

**滑动顺序：将对象“滑动”到堆的一端，从而“挤出”垃圾，可以保持对象在堆中原有的顺序**



**双指针回收算法：实现简单且速度快，但会打乱对象的原有布局，属于随机整理**

1. 整理前：两根指针分别位于内存的首尾段
2. 第一次遍历：移动位置但是并不更新标记
3. 第二次遍历：更新标记

**Lisp2算法（滑动整理算法）：需要在对象头用一个额外的槽来保存迁移完的地址**

整理前：他是一个三指针算法，并且可以处理不同大小的对象。但是需要三次遍历，并且由于对象大小不一样，所以需要额外的空间存储，而不是直接移动

![image-20240611204006644](https://gitee.com/zhf19970510/image-server/raw/master/img_2024/20240611204015.png)

第一次遍历：Free指针是为了留位置，而Scan对象是为了找存活对象

![image-20240611204102234](https://gitee.com/zhf19970510/image-server/raw/master/img_2024/20240611204102.png)

第二次遍历：更新对象地址

第三次遍历：移动对象

**引线整理算法：可以在不引入额外空间开销的情况下实现滑动整理，但需要2次遍历堆，且遍历成本较高**

**单次遍历算法：滑动回收，实时计算出对象的转发地址而不需要额外的开销**

**单次遍历算法的重点在于提前记录我们需要转移的位置**

关键词：偏移向量，标记向量以及内存索引号

**总结：**

**所有现代的标记-整理回收器均使用滑动整理，它不会改变对象的相对顺序，也就不会影响赋值器的空间局部性。复制式回收器甚至可以通过改变对象布局的方式，将对象与其父节点或者兄弟节点排列的更近以提高赋值器的空间局部性。**

**限制：**

**整理算法的限制，如任意顺序算法只能处理单一大小的对象，或者针对大小不同的对象需要分批处理；整理过程需要2次或者3次遍历堆空间；对象头部可能需要一个额外的槽来保存迁移的信息。**

### 分代收集算法

**分代收集只是一种理论，一套指导方针，一套符合大多数程序运行实际情况的经验法则**，它建立在几个分代假说之上

### 分代收集三大假说

**弱分代假说：绝大多数对象朝生夕死**

**强分代假说：活得越久的对象，也就是熬过很多次垃圾回收的对象是越来越难以消亡的**

**跨代引用假说**

### 其他垃圾收集算法



部分收集：不是完整的收集整个垃圾区域，而是只回收想要的一部分。

新生代收集：Minor GC / Young GC

老年代收集：Old GC / Major GC		一般情况下，指老年代

Mixed GC: 回收整个新生代以及部分老年代的一种回收方式。



### JVM参数

#### 标准参数

-version
-help
-server
-cp

#### -X参数

-Xint     解释执行
-Xcomp    第一次使用就编译成本地代码
-Xmixed   混合模式，JVM自己来决定

#### -XX参数

使用得最多的参数类型

非标准化参数，相对不稳定，主要用于JVM调优和Debug

```
a.Boolean类型
格式：-XX:[+-]<name>            +或-表示启用或者禁用name属性
比如：-XX:+UseConcMarkSweepGC   表示启用CMS类型的垃圾回收器
	 -XX:+UseG1GC              表示启用G1类型的垃圾回收器

b.非Boolean类型
格式：-XX<name>=<value>表示name属性的值是value
比如：-XX:MaxGCPauseMillis=500
```

#### 其他参数

-Xms1000M等价于-XX:InitialHeapSize=1000M
-Xmx1000M等价于-XX:MaxHeapSize=1000M
-Xss100等价于-XX:ThreadStackSize=100

所以这块也相当于是-XX类型的参数

打印当前java版本支持的参数：

```
java -XX:+PrintFlagsFinal -version
```



### 常用参数含义

| 参数                                                         |                             含义                             |                             说明                             |
| :----------------------------------------------------------- | :----------------------------------------------------------: | :----------------------------------------------------------: |
| -XX:CICompilerCount=3                                        |                        最大并行编译数                        | 如果设置大于1，虽然编译速度会提高，但是同样影响系统稳定性，会增加JVM崩溃的可能 |
| -XX:InitialHeapSize=100M                                     |                         初始化堆大小                         |                       简写**-Xms100M**                       |
| -XX:MaxHeapSize=100M                                         |                          最大堆大小                          |                       简写**-Xms100M**                       |
| -XX:NewSize=20M                                              |                       设置年轻代的大小                       |                                                              |
| -XX:MaxNewSize=50M                                           |                        年轻代最大大小                        |                                                              |
| -XX:OldSize=50M                                              |                        设置老年代大小                        |                                                              |
| **-XX:MetaspaceSize=50M**                                    |                        设置方法区大小                        |                                                              |
| **-XX:MaxMetaspaceSize=50M**                                 |                        方法区最大大小                        |                                                              |
| -XX:+UseParallelGC                                           |                      使用UseParallelGC                       |                      新生代，吞吐量优先                      |
| -XX:+UseParallelOldGC                                        |                     使用UseParallelOldGC                     |                      老年代，吞吐量优先                      |
| -XX:+UseConcMarkSweepGC                                      |                           使用CMS                            |                     老年代，停顿时间优先                     |
| **-XX:+UseG1GC**                                             |                           使用G1GC                           |                 新生代，老年代，停顿时间优先                 |
| -XX:NewRatio                                                 |                        新老生代的比值                        | 比如-XX:Ratio=4，则表示新生代:老年代=1:4，也就是新生代占整个堆内存的1/5 |
| -XX:SurvivorRatio                                            |                    两个S区和Eden区的比值                     | 比如-XX:SurvivorRatio=8，也就是(S0+S1):Eden=2:8，也就是一个S占整个新生代的1/10 |
| **-XX:+HeapDumpOnOutOfMemoryError**                          |                      启动堆内存溢出打印                      |      当JVM堆内存发生溢出时，也就是OOM，自动生成dump文件      |
| -XX:HeapDumpPath=heap.hprof                                  |                    指定堆内存溢出打印目录                    |             表示在当前目录生成一个heap.hprof文件             |
| -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:+PrintGCDateStamps -Xloggc:g1-gc.log |                         打印出GC日志                         |           可以使用不同的垃圾收集器，对比查看GC情况           |
| **-Xss128k**                                                 |                    设置每个线程的堆栈大小                    |                    经验值是3000-5000最佳                     |
| -XX:MaxTenuringThreshold=6                                   |                    提升年老代的最大临界值                    |                         默认值为 15                          |
| -XX:InitiatingHeapOccupancyPercent                           |                启动并发GC周期时堆内存使用占比                | G1之类的垃圾收集器用它来触发并发GC周期,基于整个堆的使用率,而不只是某一代内存的使用比. 值为 0 则表示”一直执行GC循环”. 默认值为 45. |
| -XX:G1HeapWastePercent                                       |                    允许的浪费堆空间的占比                    | 默认是10%，如果并发标记可回收的空间小于10%,则不会触发MixedGC。 |
| **-XX:MaxGCPauseMillis=200ms**                               |                        G1最大停顿时间                        | 暂停时间不能太小，太小的话就会导致出现G1跟不上垃圾产生的速度。最终退化成Full GC。所以对这个参数的调优是一个持续的过程，逐步调整到最佳状态。 |
| -XX:ConcGCThreads=n                                          |                 并发垃圾收集器使用的线程数量                 |               默认值随JVM运行的平台不同而不同                |
| -XX:G1MixedGCLiveThresholdPercent=65                         |        混合垃圾回收周期中要包括的旧区域设置占用率阈值        |                       默认占用率为 65%                       |
| -XX:G1MixedGCCountTarget=8                                   | 设置标记周期完成后，对存活数据上限为 G1MixedGCLIveThresholdPercent 的旧区域执行混合垃圾回收的目标次数 | 默认8次混合垃圾回收，混合回收的目标是要控制在此目标次数以内  |
| -XX:G1OldCSetRegionThresholdPercent=1                        |           描述Mixed GC时，Old Region被加入到CSet中           |        默认情况下，G1只把10%的Old Region加入到CSet中         |
|                                                              |                                                              |                                                              |

### 垃圾收集器

![image-20240615090414234](https://gitee.com/zhf19970510/image-server/raw/master/img_2024/20240615090414.png)

#### Serial

```
优点：简单高效，拥有很高的单线程收集效率
缺点：收集过程需要暂停所有线程
算法：复制算法
适用范围：新生代
应用：Client模式下的默认新生代收集器
```

#### Serial Old

Serial Old收集器是Serial收集器的老年代版本，也是一个单线程收集器，不同的是采用"**标记-整理算法**"，运行过程和Serial收集器一样。

#### ParNew

可以把这个收集器理解为Serial收集器的多线程版本。

```
优点：在多CPU时，比Serial效率高。
缺点：收集过程暂停所有应用程序线程，单CPU时比Serial效率差。
算法：复制算法
适用范围：新生代
应用：运行在Server模式下的虚拟机中首选的新生代收集器
```

cpu双核以下不要用parNew，性能会比serial低。

默认开启线程数和cpu核数一致。

-xx:ParallelGCThreads 限制垃圾收集的线程数。

#### Parallel Scavenge

Parallel Scavenge收集器是一个新生代收集器，它也是使用复制算法的收集器，又是并行的多线程收集器，看上去和ParNew一样，但是Parallel Scanvenge更关注系统的**吞吐量**。

> 吞吐量=运行用户代码的时间/(运行用户代码的时间+垃圾收集时间)
>
> 比如虚拟机总共运行了100分钟，垃圾收集时间用了1分钟，吞吐量=(100-1)/100=99%。
>
> 若吞吐量越大，意味着垃圾收集的时间越短，则用户代码可以充分利用CPU资源，尽快完成程序的运算任务。

```
-XX:MaxGCPauseMillis控制最大的垃圾收集停顿时间，
-XX:GCRatio直接设置吞吐量的大小。
```

并发：垃圾收集线程与业务线程一起执行的过程。

并行：多个垃圾收集线程进行执行	STW

如果停顿时间在可控制范围之内，那么优先考虑吞吐量。

如果吞吐量在极限情况下，优先考虑停顿时间。

如果没有停顿时间要求，优先设置最大吞吐量 > 95%。然后尽可能降低停顿时间。

#### Parallel Old

Parallel Old收集器是Parallel Scavenge收集器的老年代版本，使用多线程和**标记-整理算法**进行垃圾回收，也是更加关注系统的**吞吐量**。

#### CMS

CMS(Concurrent Mark Sweep)收集器是一种以获取 `最短回收停顿时间`为目标的收集器。

采用的是"标记-清除算法",整个过程分为4步

(1)初始标记 CMS initial mark     标记GC Roots直接关联对象，不用Tracing，速度很快
(2)并发标记 CMS concurrent mark  进行GC Roots Tracing
(3)重新标记 CMS remark           修改并发标记因用户程序变动的内容
(4)并发清除 CMS concurrent sweep 清除不可达对象回收空间，同时有新垃圾产生，留着下次清理称为浮动垃圾

如果希望垃圾收集的时间变短，怎么办？

好事步骤长的全部并发，不耗时的步骤 STW.

标记 GC Root 不耗时，标记所有引用链上的存活对象耗时。

初始标记：找GC Root，标记第一个对象，不耗时，STW.

并发标记：找引用链上的对象，耗时，并发执行，会降低部分吞吐量。

重新标记：第一步所产生的垃圾标记，STW，不耗时。

CMS为什么不用标记整理算法？标记整理算法更耗时，而CMS目标获取最短回收停顿时间。

优点：并发收集、低停顿
缺点：产生大量空间碎片、并发阶段会降低吞吐量

![image-20240615213850400](https://gitee.com/zhf19970510/image-server/raw/master/img_2024/20240615213850.png)

为什么初始标记阶段使用单线程而不用多线程？

线程创建会消耗资源，初始标记仅仅是标记了GC Root以及与之直接相关联的对象，速度是非常快的。没有必要进行额外的县城 开销。

为什么我的CMS回收流程图上初始标记是单线程，为什么不使用多线程呢？

初始化标记阶段是串行的，这是JDK7的行为。JDK8以后默认是并行的，可以通过参数

-XX:+CMSParallelInitialMarkEnabled控制。

![image-20240615215418144](https://gitee.com/zhf19970510/image-server/raw/master/img_2024/20240615215418.png)

根据上图描述，需要扫描一遍Young区才能知道old 区的 A才是可达对象，CMS是针对老年代的垃圾收集器，那还要全部通过gc root扫描一遍young区，似乎违背了CMS 以获取 `最短回收停顿时间`为目标的初衷，但是一定要扫描一遍young区的gc root，以及gc root引用链上存活的对象的，不然无法知道old区的A对象是存活对象，那应该怎么做？对，先做一次minor gc。这样新生代的对象就变少了，那么我再进行垃圾回收，是不是就变快了。

这也就是为什么在并发标记的时候还可以细化分为并发预处理和可中止的预处理。

预处理可以简单理解为 young gc（但是垃圾回收是JVM自动调度的，所以我们无法控制垃圾回收，也就不知道什么时候发生young gc），把重新标记预置的工作干掉（比如记录在并发标记阶段记录哪些对象依赖关系发生变化，哪些对象需要进行清理回收），就是做一些正式工作的前置工作。但是这些事情迟早有个头，所以设置了一个时间对他进行打断。当minior gc发生后，预处理就结束了，所以有可中断的概念。所以这里CMS也提供了参数控制：

**CMSScheduleRemarkEdenSizeThreshold             默认值：2M**

**CMSScheduleRemarkEdenPenetration              默认值：50%**

这两个参数组合起来就是预清理之后，Eden空间使用超过2M的时候启动可中断的并发预清理（CMS-concurrent-abortable-preclean），到Eden空间使用率到达50%的时候中断（但不是结束），进入Remark（重新标记阶段）。

所以还有一个参数：**CMSMaxAbortablePrecleanTime** ，默认为5S

> 只要到了5S，不管发没发生Minor GC，有没有到CMSScheduleRemardEdenPenetration都会中止此阶段，进入remark。

如果在5S内还是没有执行Minor GC怎么办？

> CMS提供CMSScavengeBeforeRemark参数，使remark前强制进行一次Minor GC。

并发预处理和可中断的并发预处理算事并发标记的策略，所以没有算在主流程当中。

### 记忆集

当我们进行young gc时，我们的**gc roots除了常见的栈引用、静态变量、常量、锁对象、class对象**这些常见的之外，如果 **老年代有对象引用了我们的新生代对象** ，那么老年代的对象也应该加入gc roots的范围中，但是如果每次进行young gc我们都需要扫描一次老年代的话，那我们进行垃圾回收的代价实在是太大了，因此我们引入了一种叫做记忆集的抽象数据结构来记录这种引用关系。

记忆集是一种用于记录从非收集区域指向收集区域的指针集合的数据结构。

但是如果老年代所有对象都指向了新生代，记忆集存储的对象就太多了，那么该如何解决这一问题？这时候就会引入卡表的概念。

### 卡表

记忆集是我们针对于跨代引用问题提出的思想，而卡表则是针对于该种思想的具体实现。（可以理解为记忆集是结构，卡表是实现类）

```
在hotspot虚拟机中，卡表是一个字节数组，数组的每一项对应着内存中的某一块连续地址的区域，如果该区域中有引用指向了待回收区域的对象，卡表数组对应的元素将被置为1，没有则置为0；
```

但是初始标记、并发标记、重新标记、并发清理只是CMS垃圾回收模式的一种。

(1)  卡表是使用一个字节数组实现:CARD_TABLE[],每个元素对应着其标识的内存区域一块特定大小的内存块,称为"卡页"。hotSpot使用的卡页是2^9大小,即512字节

(2)  一个卡页中可包含多个对象,只要有一个对象的字段存在跨代指针,其对应的卡表的元素标识就变成1,表示该元素变脏,否则为0。GC时,只要筛选本收集区的卡表中变脏的元素加入GC Roots里。

**卡表其他作用：**

老年代识别新生代的时候

对应的card table被标识为相应的值（card table中是一个byte，有八位，约定好每一位的含义就可区分哪个是引用新生代，哪个是并发标记阶段修改过的）

### **Foregroud CMS：**

其实这个也是CMS一种收集模式，但是他是并发失败才会走的模式。这里聊到一个概念，什么是并发失败呢？

并发失败官方的描述是：

> 如果 **并发搜集器不能在年老代填满之前完成不可达（unreachable）对象的回收** ，或者 **年老代中有效的空闲内存空间不能满足某一个内存的分配请求** ，此时应用会被暂停，并在此暂停期间开始垃圾回收，直到回收完成才会恢复应用程序。这种无法并发完成搜集的情况就成为 **并发模式失败（concurrent mode failure）** ，而且这种情况的发生也意味着我们需要调节并发搜集器的参数了。

简单来说，也就是我去进行并发标记的时候，内存不够了，这个时候我会进入STW，并且开始全局Full GC.

那么什么时候会进行并发失败呢   换句话说，我们的难道非要满了之后才进行收集

```
-XX:CMSInitiatingOccupancyFraction  
-XX:+UseCMSInitiatingOccupancyOnly

注意：-XX:+UseCMSInitiatingOccupancyOnly 只是用设定的回收阈值(上面指定的70%),如果不指定,JVM仅在第一次使用设定值,后续则自动调整.这两个参数表示只有在Old区占了CMSInitiatingOccupancyFraction设置的百分比的内存时才满足触发CMS的条件。注意这只是满足触发CMS GC的条件。至于什么时候真正触发CMS GC，由一个后台扫描线程决定。CMSThread默认2秒钟扫描一次，判断是否需要触发CMS，这个参数可以更改这个扫描时间间隔。
```

**CMS的标记压缩算法-----MSC（**Mark Sweep Compact**）**

他的回收方式其实就是我们的滑动整理，并且进行整理的时候一般都是两个参数

```
-XX:+UseCMSCompactAtFullCollection 
-XX:CMSFullGCsBeforeCompaction=0
这两个参数表示多少次FullGC后采用MSC算法压缩堆内存，0表示每次FullGC后都会压缩，同时0也是默认值
```

碎片问题也是CMS采用的标记清理算法最让人诟病的地方：Backgroud CMS采用的标记清理算法会导致内存碎片问题，从而埋下发生FullGC导致长时间STW的隐患。

所以如果触发了FullGC，无论是否会采用MSC算法压缩堆，那都是ParNew+CMS组合非常糟糕的情况。因为这个时候并发模式已经搞不定了，而且整个过程单线程，完全STW，可能会压缩堆（是否压缩堆通过上面两个参数控制），真的不能再糟糕了！想象如果这时候业务量比较大，由于FullGC导致服务完全暂停几秒钟，甚至上10秒，对用户体验影响得多大。

### 三色标记

在并发标记的过程中，因为标记期间应用线程还在继续跑，对象间的引用可能发生变化，多标和漏标的情况就有可能发生。这里引入“三色标记”来给大家解释下，把Gcroots可达性分析遍历对象过程中遇到的对象， 按照“是否访问过”这个条件标记成以下三种颜色：

**黑色：**

```
表示对象已经被垃圾收集器访问过， 且这个对象的所有引用都已经扫描过。 黑色的对象代表已经扫描过， 它是安全存活的， 如果有其他对象引用指向了黑色对象， 无须重新扫描一遍。 黑色对象不可能直接（不经过灰色对象） 指向某个白色对象。
```

**灰色：**

```
表示对象已经被垃圾收集器访问过， 但这个对象上至少存在一个引用还没有被扫描过。
```

**白色:**

```
表示对象尚未被垃圾收集器访问过。 显然在可达性分析刚刚开始的阶段， 所有的对象都是白色的， 若在分析结束的阶段， 仍然是白色的对象， 即代表不可达。
```

标记过程：

1.初始时，所有对象都在 【白色集合】中；

2.将GC Roots 直接引用到的对象 挪到 【灰色集合】中；

3.从灰色集合中获取对象：

4. 将本对象 引用到的 其他对象 全部挪到 【灰色集合】中；
5. 将本对象 挪到 【黑色集合】里面。

重复步骤3.4，直至【灰色集合】为空时结束。

结束后，仍在【白色集合】的对象即为GC Roots 不可达，可以进行回收

**多标-浮动垃圾**

```
在并发标记过程中，如果由于方法运行结束导致部分局部变量(gcroot)被销毁，这个gcroot引用的对象之前又被扫描过 (被标记为非垃圾对象)，那么本轮GC不会回收这部分内存。这部分本应该回收但是没有回收到的内存，被称之为“浮动 垃圾”。浮动垃圾并不会影响垃圾回收的正确性，只是需要等到下一轮垃圾回收中才被清除。

另外，针对并发标记(还有并发清理)开始后产生的新对象，通常的做法是直接全部当成黑色，本轮不会进行清除。这部分 对象期间可能也会变为垃圾，这也算是浮动垃圾的一部分。
```

**漏标-读写屏障**

漏标只有**同时满足**以下两个条件时才会发生：

```
条件一：灰色对象 断开了 白色对象的引用；即灰色对象 原来成员变量的引用 发生了变化。

条件二：黑色对象 重新引用了 该白色对象；即黑色对象 成员变量增加了 新的引用。
```

漏标会导致被引用的对象被当成垃圾误删除，这是严重bug，必须解决，有两种解决方案：  **增量更新（Incremental Update） 和原始快照（Snapshot At The Beginning，SATB）** 。

**增量更新**就是当黑色对象**插入新的指向**白色对象的引用关系时， 就将这个新插入的引用记录下来， 等并发扫描结束之后， 再将这些记录过的引用关系中的黑色对象为根， 重新扫描一次。 这可以简化理解为， 黑色对象一旦新插入了指向白色对象的引用之后， 它就变回灰色对象了。

**原始快照**就是当灰色对象要**删除指向**白色对象的引用关系时， 就将这个要删除的引用记录下来， 在并发扫描结束之后， 再将这些记录过的引用关系中的灰色对象为根， 重新扫描一次，这样就能扫描到白色的对象，将白色对象直接标记为黑色(目的就是让这种对象在本轮gc清理中能存活下来，待下一轮gc的时候重新扫描，这个对象也有可能是浮动垃圾)

以上**无论是对引用关系记录的插入还是删除， 虚拟机的记录操作都是通过写屏障实现的。**

**写屏障实现原始快照（SATB）：** 当对象B的成员变量的引用发生变化时，比如引用消失（a.b.d = null），我们可以利用写屏障，将B原来成员变量的引用对象D记录下来：

**写屏障实现增量更新：** 当对象A的成员变量的引用发生变化时，比如新增引用（a.d = d），我们可以利用写屏障，将A新的成员变量引用对象D 记录下来：

### CMS标记清除的全局整理：

由于CMS使用的是标记清除算法，而标记清除算法会有大量的内存碎片的产生，所以JVM提供了

**-XX:+UseCMSCompactAtFullCollection**参数用于在全局GC（full GC）后进行一次碎片整理的工作，

由于每次全局GC后都进行碎片整理会较大的影响停顿时间，JVM又提供了参数

**-XX:CMSFullGCsBeforeCompaction**去 **控制在几次全局GC后会进行碎片整理** 。

## CMS常用参数含义：

**-XX:+UseConcMarkSweepGC**

打开CMS GC收集器。JVM在1.8之前默认使用的是Parallel GC，9以后使用G1 GC。

**-XX:+UseParNewGC**

当使用CMS收集器时，默认年轻代使用多线程并行执行垃圾回收（UseConcMarkSweepGC开启后则默认开启）。

**-XX:+CMSParallelRemarkEnabled**

采用并行标记方式降低停顿（默认开启）。

**-XX:+CMSConcurrentMTEnabled**

被启用时，并发的CMS阶段将以多线程执行（因此，多个GC线程会与所有的应用程序线程并行工作）。（默认开启）

**-XX:ConcGCThreads**

定义并发CMS过程运行时的线程数。

**-XX:ParallelGCThreads**

定义CMS过程并行收集的线程数。

**-XX:CMSInitiatingOccupancyFraction**

该值代表老年代堆空间的使用率，默认值为68。当老年代使用率达到此值之后，并行收集器便开始进行垃圾收集，该参数需要配合UseCMSInitiatingOccupancyOnly一起使用，单独设置无效。

**-XX:+UseCMSInitiatingOccupancyOnly**

该参数启用后，参数CMSInitiatingOccupancyFraction才会生效。默认关闭。

**-XX:+CMSClassUnloadingEnabled**

相对于并行收集器，CMS收集器默认不会对永久代进行垃圾回收。如果希望对永久代进行垃圾回收，可用设置-XX:+CMSClassUnloadingEnabled。默认关闭。

**-XX:+CMSIncrementalMode**

开启CMS收集器的增量模式。增量模式使得回收过程更长，但是暂停时间往往更短。默认关闭。

**-XX:CMSFullGCsBeforeCompaction**

设置在执行多少次Full GC后对内存空间进行压缩整理，默认值0。

**-XX:+CMSScavengeBeforeRemark**

在cms gc remark之前做一次ygc，减少gc roots扫描的对象数，从而提高remark的效率，默认关闭。

**-XX:+ExplicitGCInvokesConcurrent**

该参数启用后JVM无论什么时候调用系统GC，都执行CMS GC，而不是Full GC。

**-XX:+ExplicitGCInvokesConcurrentAndUnloadsClasses**

该参数保证当有系统GC调用时，永久代也被包括进CMS垃圾回收的范围内。

**-XX:+DisableExplicitGC**

该参数将使JVM完全忽略系统的GC调用（不管使用的收集器是什么类型）。

**-XX:+UseCompressedOops**

这个参数用于对类对象数据进行压缩处理，提高内存利用率。（默认开启）

**-XX:MaxGCPauseMillis=200**

这个参数用于设置GC暂停等待时间，单位为毫秒，不要设置过低。

## CMS的线程数计算公式

区分young区的parnew gc线程数和old区的cms线程数，分别为以下两参数：

* -XX:ParallelGCThreads=m       // STW暂停时使用的GC线程数，一般用满CPU
* -XX:ConcGCThreads=n            // GC线程和业务线程并发执行时使用的GC线程数，一般较小

### ParallelGCThreads

其中ParallelGCThreads 参数的默认值是：

* CPU核心数 <= 8，则为 ParallelGCThreads=CPU核心数，比如4C8G取4，8C16G取8
* CPU核心数 > 8，则为 ParallelGCThreads = CPU核心数 * 5/8 + 3 向下取整
* 16核的情况下，ParallelGCThreads = 13
* 32核的情况下，ParallelGCThreads = 23
* 64核的情况下，ParallelGCThreads = 43
* 72核的情况下，ParallelGCThreads = 48

**ConcGCThreads**

ConcGCThreads的默认值则为：

ConcGCThreads = (ParallelGCThreads + 3)/4 向下取整。

* ParallelGCThreads = 1~4时，ConcGCThreads = 1
* ParallelGCThreads = 5~8时，ConcGCThreads = 2
* ParallelGCThreads = 13~16时，ConcGCThreads = 4

## CMS推荐配置参数：

第一种情况：8C16G左右服务器，再大的服务器可以上G1了  没必要

```
-Xmx12g -Xms12g
-XX:ParallelGCThreads=8
-XX:ConcGCThreads=2
-XX:+UseConcMarkSweepGC
-XX:+CMSClassUnloadingEnabled
-XX:+CMSIncrementalMode
-XX:+CMSScavengeBeforeRemark
-XX:+UseCMSInitiatingOccupancyOnly
-XX:CMSInitiatingOccupancyFraction=70
-XX:CMSFullGCsBeforeCompaction=5
-XX:MaxGCPauseMillis=100  // 按业务情况来定
-XX:+ExplicitGCInvokesConcurrent
-XX:+ExplicitGCInvokesConcurrentAndUnloadsClasses
-XX:+PrintGCTimeStamps
-XX:+PrintGCDetails
-XX:+PrintGCDateStamps
```

第二种情况：4C8G

```
-Xmx6g -Xms6g
-XX:ParallelGCThreads=4
-XX:ConcGCThreads=1
-XX:+UseConcMarkSweepGC
-XX:+CMSClassUnloadingEnabled
-XX:+CMSIncrementalMode
-XX:+CMSScavengeBeforeRemark
-XX:+UseCMSInitiatingOccupancyOnly
-XX:CMSInitiatingOccupancyFraction=70
-XX:CMSFullGCsBeforeCompaction=5
-XX:MaxGCPauseMillis=100  // 按业务情况来定
-XX:+ExplicitGCInvokesConcurrent
-XX:+ExplicitGCInvokesConcurrentAndUnloadsClasses
-XX:+PrintGCTimeStamps
-XX:+PrintGCDetails
-XX:+PrintGCDateStamps
```

第三种情况：2C4G，这种情况下，也不推荐使用，因为2C的情况下，线程上下文的开销比较大，性能可能还不如你不动的情况，没必要。非要用，给你个配置，你自己玩。

```
-Xmx3g -Xms3g
-XX:ParallelGCThreads=2
-XX:ConcGCThreads=1
-XX:+UseConcMarkSweepGC
-XX:+CMSClassUnloadingEnabled
-XX:+CMSIncrementalMode
-XX:+CMSScavengeBeforeRemark
-XX:+UseCMSInitiatingOccupancyOnly
-XX:CMSInitiatingOccupancyFraction=70
-XX:CMSFullGCsBeforeCompaction=5
-XX:MaxGCPauseMillis=100  // 按业务情况来定
-XX:+ExplicitGCInvokesConcurrent
-XX:+ExplicitGCInvokesConcurrentAndUnloadsClasses
-XX:+PrintGCTimeStamps
-XX:+PrintGCDetails
-XX:+PrintGCDateStamps
```



**JDK8为什么不用CMS做为默认垃圾收集器呢**

**1.CMS单线程或者双线程情况下效率很低**

**2.CMS会并发失败**

**3.CMS可中止的预处理会导致极限5S停顿**

**4.并发失败进入foregroud还会导致进入Full GC,全局MSC整理**

**5.CMS吞吐的设计并不是很优秀**



#### G1(Garbage-First)

G1的目的：Garbage First，也就是垃圾优先原则，也就是空间方面的关注点。同时照顾到停顿时间以及吞吐量。

G1垃圾收集器的设计目的是避免FULL GC，但是当并发收集不能足够快地回收内存时，就会发生FULL GC。G1的FULL GC的当前实现使用单线程MSC算法。

**G1的特点**:   garbage  First
内存空间的重新定义
更短的停顿时间    要多短就多短
某种程度上去解决空间碎片

使用G1收集器时，Java堆的内存布局与就与其他收集器有很大差别，它将整个Java堆划分为多个大小相等的独立区域（Region），虽然还保留有新生代和老年代的概念，但新生代和老年代不再是物理隔离的了，它们都是一部分Region（不需要连续）的集合。

每个Region大小都是一样的，可以是1M到32M之间的数值，但是必须保证是2的n次幂

如果对象太大，一个Region放不下[超过Region大小的50%]，那么就会直接放到H中

设置Region大小：-XX:G1HeapRegionSize=<N>M

所谓Garbage-Frist，其实就是优先回收垃圾最多的Region区域

![image-20240616205404971](https://gitee.com/zhf19970510/image-server/raw/master/img_2024/20240616205405.png)

```
（1）分代收集（仍然保留了分代的概念）
（2）空间整合（整体上属于“标记-整理”算法，不会导致空间碎片）
（3）可预测的停顿（比CMS更先进的地方在于能让使用者明确指定一个长度为M毫秒的时间片段内，消耗在垃圾收集上的时间不得超过N毫秒）
```

工作过程可以分为如下几步：前3个阶段和CMS差不多，最后一个阶段是G1垃圾收集器特有的，它可以预估回收价值，将价值高的区域优先回收，这是G1垃圾收集器的一大特点。

```
初始标记（Initial Marking）      标记以下GC Roots能够关联的对象，并且修改TAMS的值，需要暂停用户线程
并发标记（Concurrent Marking）   从GC Roots进行可达性分析，找出存活的对象，与用户线程并发执行
最终标记（Final Marking）        修正在并发标记阶段因为用户程序的并发执行导致变动的数据，需暂停用户线程
筛选回收（Live Data Counting and Evacuation） 对各个Region的回收价值和成本进行排序，根据用户所期望的GC停顿时间制定回收计划
```

![image-20240615204629856](https://gitee.com/zhf19970510/image-server/raw/master/img_2024/20240615204629.png)

**Region角色**：
自由角色FreeTag
新生代分区 YoungHeapRegion，细分为eden分区和survivor分区
大对象分区 HHR，细分为大对象头分区和大对象连续分区
老年代分区 OHR
JDK11之后有一类特殊的分区，叫做归档分区，关闭归档分区以及开放归档分区

**TLAB（线程本地分配缓冲区 Thread Local Allocation Buffer）**

分配空间时，为了提高JVM的运行效率，应当尽量减少临界区范围，避免全局锁。G1的通常的应用场景中，会存在大量的访问器同时执行，为减少锁冲突，JVM引入了TLAB（线程本地分配缓冲区 Thread Local Allocation Buffer）机制。

![image-20240616212402706](https://gitee.com/zhf19970510/image-server/raw/master/img_2024/20240616212403.png)

**RSet（RemeberSet）引用集**

**稀疏表**

**稀疏表：本质上就是一种Hash表，Key是Region的起始地址，Value是一个数组，里面存储的元素是卡页的索引号。**

![image-20240616213119217](https://gitee.com/zhf19970510/image-server/raw/master/img_2024/20240616213119.png)

**粗粒度位图**

粗粒度位图：当细粒度位图 size超过阈值时，所有region 形成一个 bitMap。如果有region 对当前 Region 有指针指向，就设置其对应的bit 为1，也就是粗粒度位图.

![image-20240616214633321](https://gitee.com/zhf19970510/image-server/raw/master/img_2024/20240616214633.png)

**细粒度位图**

细粒度位图：就是一个C位图，但是这个位图，可以详细的记录我们的内存变化，包括并发标记修改，对应元素标识等
当稀疏表指定region的card数量超过阈值时，则在细粒度位图中创建一个对应的PerRegionTable对象。一个Region地址链表，维护当前Region中所有card对应的一个BitMap集合。

![image-20240616214050752](https://gitee.com/zhf19970510/image-server/raw/master/img_2024/20240616214050.png)

**什么是写屏障：因为Store Buffer导致读写的顺序不一致，而写屏障可以解决这个问题**

![image-20240616215026383](https://gitee.com/zhf19970510/image-server/raw/master/img_2024/20240616215026.png)

**写屏障的内存伪共享问题：**

**如果不同线程对对象引用的更新操作，恰好位于同一个64KB区域内，这将导致同时更新卡表的同一个缓存行，从而造成缓存行的写回、无效化或者同步操作，间接影响程序性能。**

**解决方案：**

**不采用无条件的写屏障，而是先检查卡表标记，只有当该卡表项未被标记过才将其标记为dirty。**

**这就是JDK 7中引入的解决方法，引入了一个新的JVM参数-XX:+UseCondCardMark.**



**G1的Rset同步异步问题：**

**异步更新操作需要引入DCQS（Dirty Card Queue Set）结构。**

**JVM声明了一个全局的静态结构G1BarrierSet，其中包含两个Queue Set，DirtyCardQueueSet和G1SATBMarkQueueSet，分别用于处理DCQS和STAB**

**G1常用参数：**

**-XX: +UseG1GC 开启G1垃圾收集器**

**-XX: G1HeapReginSize 设置每个Region的大小，是2的幂次，1MB-32MB之间**

**-XX:MaxGCPauseMillis 最大停顿时间**

**-XX:ParallelGCThread 并行GC工作的线程数**

**-XX:ConcGCThreads 并发标记的线程数**

**-XX:InitiatingHeapOcccupancyPercent 默认45%，代表GC堆占用达到多少的时候开始垃圾收集**



## G1垃圾收集器的三种模式

### young GC

young GC的触发条件

Eden区的大小范围 = [ -XX:G1NewSizePercent, -XX:G1MaxNewSizePercent ] = [ 整堆5%, 整堆60% ]
在[ 整堆5%, 整堆60% ]的基础上，G1会计算下现在Eden区回收大概要多久时间，如果回收时间远远小于参数-XX:MaxGCPauseMills设定的值（默认200ms），那么增加年轻代的region，继续给新对象存放，不会马上做YoungGC。
G1计算回收时间接近参数-XX:MaxGCPauseMills设定的值，那么就会触发YoungGC。

### mixed   GC

混合式回收主要分为如下子阶段：

* 初始标记子阶段
* 并发标记子阶段
* 再标记子阶段
* 清理子阶段
* 垃圾回收

#### 是否进入并发标记判定

* YGC最后阶段判断是否启动并发标记
* 判断的依据是分配和即将分配的内存占比是否大于阈值
* 阈值受JVM参数InitiatingHeapOccupancyPercent控制，默认45

#### ZGC

JDK11新引入的ZGC收集器，不管是物理上还是逻辑上，ZGC中已经不存在新老年代的概念了

会分为一个个page，当进行GC操作时会对page进行压缩，因此没有碎片问题

只能在64位的linux上使用，目前用得还比较少。

（1）可以达到10ms以内的停顿时间要求

（2）支持TB级别的内存

（3）堆内存变大后停顿时间还是在10ms以内



## ZGC三大核心技术

### 多重映射

ZGC为了能高效、灵活地管理内存，实现了两级内存管理：虚拟内存和物理内存，并且实现了物理内存和虚拟内存的映射关系。这和操作系统中虚拟地址和物理地址设计思路基本一致。

当应用程序创建对象时，首先在堆空间申请一个虚拟地址，ZGC同时会为该对象在Marked0、Marked1和Remapped三个视图空间分别申请一个虚拟地址，且这三个虚拟地址对应同一个物理地址。

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/1463/1654493994032/f01df7b0647342488258314e249c954f.png)

为什么这么设计呢？

这就是ZGC的高明之处，利用虚拟空间换时间，这三个空间的切换是由垃圾回收的不同阶段触发的，通过限定三个空间在同一时间点有且仅有一个空间有效高效的完成GC过程的并发操作.



### 读屏障

请各位注意，之前我们一直在聊写屏障，而这里聊到读屏障，很多同学就会把这个读屏障跟java内存模型的读屏障混淆。这里的读屏障实际上指的是一种手段，并且是一种类似于AOP的手段。

我们之前聊的写屏障是数据写入时候的屏障，而java内存屏障中的读屏障实际上也是类似的。

但是在ZGC中的读屏障，则是JVM向应用代码插入一小段代码的技术，当应用线程从堆中读取对象引用时，就会执行这段代码。他跟我们的java内存屏障中的读屏障根本就不是一个东西。他是在字节码层面或者编译代码层面给读操作增加一个额外的处理。一个类似与面向切面的处理。

并且ZGC的读屏障是只有从中读取对象引用，才需要加入读屏

**读屏障案例：**

```
Object o = obj.FieldA      // 从堆中读取对象引用，需要加入读屏障
<load barrier needed here>
  
Object p = o               // 无需加入读屏障，因为不是从堆中读取引用
o.dosomething()            // 无需加入读屏障，因为不是从堆中读取引用
int i =  obj.FieldB        // 无需加入读屏障，因为不是对象引用
```

**那么我加上这个读屏障有什么作用呢？**

这里我们思考下：

由于GC线程和应用线程是并发执行的，所以肯定会存在应用线程去A对象内部的引用所指向的对象B的时候，这个对象B正在被GC线程移动或者其他操作，加上读屏障之后，应用线程会去探测对象B是否被GC线程操作，然后等待操作完成再读取对象，确保数据的准确性。这个操作强依赖于前面的多种映射。



具体探查操作图：

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/1463/1654493994032/ec4025d0c8a44b91a98ead2e4ff01a51.png)

问题：如此复杂的探查操作会不会影响程序的性能呢？

会，据测试，最多百分之4的性能损耗。但这是ZGC并发转移的基础，为了降低STW，设计者认为这点牺牲是可接受的。



### 指针染色

读屏障以及指针染色是我们能够实现并发转移的核心技术之一，也是关键所在。

在讲ZGC并发处理算法之前，还需要补充一个知识点——染色指针。

我们都知道，之前的垃圾收集器都是把GC信息（标记信息、GC分代年龄…）存在对象头的Mark Word里。举个例子：

如果某个物品是个垃圾，就在这个物品上盖一个“垃圾”的章；如果这个物品不是垃圾了，就把这个物品上的“垃圾”印章洗掉。

而ZGC是这样做的：

如果某个物品是垃圾。就在这个物品的信息或者标签里面标注这个物品是个垃圾，以后不管这个物品在哪扫描，快递到哪，别人都知道他是个垃圾了。也许哪一天，这个物品不再是垃圾，比如收废品的小王，觉得比如这个物品有利用价值。就把这个物品标签信息里面的“垃圾”标志去掉。

在这例子中，“这个物品”就是一个对象，而“标签”就是指向这个对象的指针。

ZGC将信息存储在指针中，这种技术有一个高大上的名字——染色指针（Colored Pointer）

原理：
Linux下64位指针的高18位不能用来寻址，但剩余的46位指针所能支持的64TB内存在今天仍然能够充分满足大型服务器的需要。鉴于此，ZGC的染色指针技术继续盯上了这剩下的46位指针宽度，将其高4位提取出来存储四个标志信息。通过这些标志位，虚拟机可以直接从指针中看到其引用对象的三色标记状态、是否进入了重分配集（即被移动过）、是否只能通过finalize()方法才能被访问到。当然，由于这些标志位进一步压缩了原本就只有46位的地址空间，也直接导致
ZGC能够管理的内存不可以超过4TB（2的42次幂)  当然，后续的版本可以了，因为开发了更多的位数。前面是觉得没必要，够大了。

而后续开发变成了这个样子：

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/1463/1654493994032/7edd93a169d6426fa6775c620d39df58.png)

在64位的机器中，对象指针是64位的。

* ZGC使用64位地址空间的第0~43位存储对象地址，2^44 = 16TB，所以ZGC最大支持16TB的堆。
* 而第44~47位作为颜色标志位，Marked0、Marked1和Remapped代表三个视图标志位，Finalizable表示这个对象只能通过finalizer才能访问。
* 第48~63位固定为0没有利用。





## ZGC并发处理算法

GC并发处理算法利用全局空间视图的切换和对象地址视图的切换，结合SATB算法实现了高效的并发。

相比于 Java 原有的百毫秒级的暂停的 Parallel GC 和 G1，以及未解决碎片化问题的 CMS ，并发和压缩式的 ZGC 可谓是 Java GC 能力的一次重大飞跃—— GC 线程在整理内存的同时，可以让 Java 线程继续执行。 ZGC 采用标记-压缩策略来回收 Java 堆：ZGC 首先会并发标记( concurrent mark )堆中的活跃对象，然后并发转移( concurrent relocate )将部分区域的活跃对象整理到一起。这里与早先的 Java GC 不同之处在于，目前 ZGC 是单代垃圾回收器，在标记阶段会遍历堆中的全部对象。

ZGC的并发处理算法三个阶段的全局视图切换如下：

初始化阶段：ZGC初始化之后，整个内存空间的地址视图被设置为Remapped
标记阶段：当进入标记阶段时的视图转变为Marked0（以下皆简称M0）或者Marked1（以下皆简称M1）
转移阶段：从标记阶段结束进入转移阶段时的视图再次设置为Remapped

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/1463/1654493994032/e984fc2813214b48aa6afc5b45cda169.png)

### 标记阶段

标记阶段全局视图切换到M0视图。因为应用程序和标记线程并发执行，那么对象的访问可能来自标记线程和应用程序线程。

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/1463/1654493994032/0fdbce86bb644d078659970f4571a0bc.png)

在标记阶段结束之后，对象的地址视图要么是M0，要么是Remapped。

如果对象的地址视图是M0，说明对象是活跃的；
如果对象的地址视图是Remapped，说明对象是不活跃的，即对象所使用的内存可以被回收。
当标记阶段结束后，ZGC会把所有活跃对象的地址存到对象活跃信息表，活跃对象的地址视图都是M0。

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/1463/1654493994032/528cd40547e24c06a4faa0a2bbf53b77.png)

### 转移阶段

转移阶段切换到Remapped视图。因为应用程序和转移线程也是并发执行，那么对象的访问可能来自转移线程和应用程序线程。

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/1463/1654493994032/37d03238115941a8a6fd5eab2b971404.png)

至此，ZGC的一个垃圾回收周期中，并发标记和并发转移就结束了。



**为何要设计M0和M1**
我们提到在标记阶段存在两个地址视图M0和M1，上面的算法过程显示只用到了一个地址视图，为什么设计成两个？简单地说是为了区别前一次标记和当前标记。

ZGC是按照页面进行部分内存垃圾回收的，也就是说当对象所在的页面需要回收时，页面里面的对象需要被转移，如果页面不需要转移，页面里面的对象也就不需要转移。![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/1463/1654493994032/075521da81b647babfe5cb4ff2eb2da7.png)

如图，这个对象在第二次GC周期开始的时候，地址视图还是M0。如果第二次GC的标记阶段还切到M0视图的话，就不能区分出对象是活跃的，还是上一次垃圾回收标记过的。这个时候，第二次GC周期的标记阶段切到M1视图的话就可以区分了，此时这3个地址视图代表的含义是：

* M1：本次垃圾回收中识别的活跃对象。
* M0：前一次垃圾回收的标记阶段被标记过的活跃对象，对象在转移阶段未被转移，但是在本次垃圾回收中被识别为不活跃对象。
* Remapped：前一次垃圾回收的转移阶段发生转移的对象或者是被应用程序线程访问的对象，但是在本次垃圾回收中被识别为不活跃对象。

### ZGC并发处理演示图

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/1463/1654493994032/a15da54676fa4320a134d4d9d6ceb236.png)



**使用地址视图和染色指针有什么好处？**

使用地址视图和染色指针可以加快标记和转移的速度。以前的垃圾回收器通过修改对象头的标记位来标记GC信息，这是有内存存取访问的，而ZGC通过地址视图和染色指针技术，无需任何对象访问，只需要设置地址中对应的标志位即可。这就是ZGC在标记和转移阶段速度更快的原因。

当GC信息不再存储在对象头上时而存在引用指针上时，当确定一个对象已经无用的时候，可以立即重用对应的内存空间，这是把GC信息放到对象头所做不到的。

ZGC只有三个STW阶段：初始标记，再标记，初始转移。

其中，初始标记和初始转移分别都只需要扫描所有GC Roots，其处理时间和GC Roots的数量成正比，一般情况耗时非常短；

再标记阶段STW时间很短，最多1ms，超过1ms则再次进入并发标记阶段。即，ZGC几乎所有暂停都只依赖于GC Roots集合大小，停顿时间不会随着堆的大小或者活跃对象的大小而增加。与ZGC对比，G1的转移阶段完全STW的，且停顿时间随存活对象的大小增加而增加。



### ZGC最佳调优参数：

* `-Xms -Xmx`：堆的最大内存和最小内存，这里都设置为16G，程序的堆内存将保持16G不变。
* `-XX:ReservedCodeCacheSize -XX:InitialCodeCacheSize`：设置CodeCache的大小， JIT编译的代码都放在CodeCache中，一般服务64m或128m就已经足够。我们的服务因为有一定特殊性，所以设置的较大，后面会详细介绍。
* `-XX:+UnlockExperimentalVMOptions -XX:+UseZGC`：启用ZGC的配置。
* `-XX:ConcGCThreads`：并发回收垃圾的线程。默认是总核数的12.5%，8核CPU默认是1。调大后GC变快，但会占用程序运行时的CPU资源，吞吐会受到影响。
* `-XX:ParallelGCThreads`：STW阶段使用线程数，默认是总核数的60%。
* `-XX:ZCollectionInterval`：ZGC发生的最小时间间隔，单位秒。
* `-XX:ZAllocationSpikeTolerance`：ZGC触发自适应算法的修正系数，默认2，数值越大，越早的触发ZGC。
* `-XX:+UnlockDiagnosticVMOptions -XX:-ZProactive`：是否启用主动回收，默认开启，这里的配置表示关闭。
* `-Xlog`：设置GC日志中的内容、格式、位置以及每个日志的大小。

```
-Xms16G -Xmx16G 
-XX:ReservedCodeCacheSize=256m -XX:InitialCodeCacheSize=256m 
-XX:+UnlockExperimentalVMOptions -XX:+UseZGC 
-XX:ConcGCThreads=2 -XX:ParallelGCThreads=6 
-XX:ZCollectionInterval=120 -XX:ZAllocationSpikeTolerance=5 
-XX:+UnlockDiagnosticVMOptions -XX:-ZProactive 
-Xlog:safepoint,classhisto*=trace,age*,gc*=info:file=/opt/logs/logs/gc-%t.log:time,tid,tags:filecount=5,filesize=50m 
```

## ZGC垃圾回收触发时机

> 相比于CMS和G1的GC触发机制，ZGC的GC触发机制有很大不同。ZGC的核心特点是并发，GC过程中一直有新的对象产生。如何保证在GC完成之前，新产生的对象不会将堆占满，是ZGC参数调优的第一大目标。因为在ZGC中，当垃圾来不及回收将堆占满时，会导致正在运行的线程停顿，持续时间可能长达秒级之久。

ZGC有多种GC触发机制，总结如下：

* **阻塞内存分配请求触发** ：当垃圾来不及回收，垃圾将堆占满时，会导致部分线程阻塞。我们应当避免出现这种触发方式。日志中关键字是“Allocation Stall”。
* **基于分配速率的自适应算法** ：最主要的GC触发方式，其算法原理可简单描述为”ZGC根据近期的对象分配速率以及GC时间，计算出当内存占用达到什么阈值时触发下一次GC”。自适应算法的详细理论可参考彭成寒《新一代垃圾回收器ZGC设计与实现》一书中的内容。通过ZAllocationSpikeTolerance参数控制阈值大小，该参数默认2，数值越大，越早的触发GC。我们通过调整此参数解决了一些问题。日志中关键字是“Allocation Rate”。
* **基于固定时间间隔** ：通过ZCollectionInterval控制，适合应对突增流量场景。流量平稳变化时，自适应算法可能在堆使用率达到95%以上才触发GC。流量突增时，自适应算法触发的时机可能会过晚，导致部分线程阻塞。我们通过调整此参数解决流量突增场景的问题，比如定时活动、秒杀等场景。日志中关键字是“Timer”。
* **主动触发规则** ：类似于固定间隔规则，但时间间隔不固定，是ZGC自行算出来的时机，我们的服务因为已经加了基于固定时间间隔的触发机制，所以通过-ZProactive参数将该功能关闭，以免GC频繁，影响服务可用性。 日志中关键字是“Proactive”。
* **预热规则** ：服务刚启动时出现，一般不需要关注。日志中关键字是“Warmup”。
* **外部触发** ：代码中显式调用System.gc()触发。 日志中关键字是“System.gc()”。
* **元数据分配触发** ：元数据区不足时导致，一般不需要关注。 日志中关键字是“Metadata GC Threshold”。



#### 垃圾收集器分类

* **串行收集器**->Serial和Serial Old

只能有一个垃圾回收线程执行，用户线程暂停。

`适用于内存比较小的嵌入式设备`。

* **并行收集器**[吞吐量优先]->Parallel Scanvenge、Parallel Old

多条垃圾收集线程并行工作，但此时用户线程仍然处于等待状态。

`适用于科学计算、后台处理等若交互场景`。

* **并发收集器**[停顿时间优先]->CMS、G1

用户线程和垃圾收集线程同时执行(但并不一定是并行的，可能是交替执行的)，垃圾收集线程在执行的时候不会停顿用户线程的运行。

`适用于相对时间有要求的场景，比如Web`。

#### 如何选择合适的垃圾收集器

* 优先调整堆的大小让服务器自己来选择
* 如果内存小于100M，使用串行收集器
* 如果是单核，并且没有停顿时间要求，使用串行或JVM自己选
* 如果允许停顿时间超过1秒，选择并行或JVM自己选
* 如果响应时间最重要，并且不能超过1秒，使用并发收集器



### JVM命令以及执行引擎

java进程	线程	内存空间	对象占用情况	类加载信息	GC信息

查看当前的系统中的java进程：

jps

jinfo -flag UseG1GC 22760



查看java进程所有参数：

>jinfo -flags 22760

![image-20240701213639601](https://gitee.com/zhf19970510/image-server/raw/master/img_2024/20240701213648.png)



进程	性能	Class信息	GC信息

 jstat -class 22760 1000 10

jstat -gc 22760 1000 10



jstack	查看当前线程信息

jstack 22760



jmap	查看堆信息

jmap -heap 22760

![image-20240702210338389](https://gitee.com/zhf19970510/image-server/raw/master/img_2024/20240702210338.png)

![image-20240702210306965](https://gitee.com/zhf19970510/image-server/raw/master/img_2024/20240702210307.png)

打印OOM报错信息：

-XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=heap.hprof



字节码转换为机器码？谁来执行这些字节码指令？

执行引擎。

![image-20240706074421793](https://gitee.com/zhf19970510/image-server/raw/master/img_2024/20240706074428.png)

#### 解释执行

Interpreter，解释器逐条把字节码翻译成机器码并执行，跨平台的保证。

刚开始执行引擎只采用了解释执行的，但是后来发现某些方法或者代码块被调用的特别频繁时，就会把这些代码认为“热点代码”。 

#### 即时编译器

Just-In-Time compilation(JIT)，及时编译器会先将字节码编译成对应平台的可执行文件，运行速度快。即时编译器会把这些热点代码编译成与本地平台关联的机器码，并且进行各层次的优化，保存到内存中。

#### JVM采用哪种方式？

JVM采用的是混合模式，也就是解释+编译的方式，对大部分不常用的代码，不需要浪费时间将其编译成机器码，只需要用到的时候再以解释的方式对小部分的热点代码，可以采取编译的方式，追求更高的运行效率。

#### 即时编译类型

（1）HotSpot虚拟机里面内置了两个JIT：C1和C2

​	C1称为Client Compiler，适用于执行时间短或者对启动性能有要求的程序

​    C2称为Server Compiler，适用于执行时间长或者对巅峰值性能有要求的程序。

（2）Java7开始，HotSpot会使用分层编译的方式

​    分层编译也就是会结合C1的启动性能优势和C2的巅峰值性能优势，热点方法会先被C1编译，然后热点方法中的热点会被C2再次编译。

-XX:+TiredCompilation开启参数



#### JVM的分层编译5大级别：

**0.解释执行**

**1.简单的C1编译**：仅仅使用我们的C1做一些简单的优化，不会开启Profiling

**2.受限的C1编译代码：**只会执行我们的方法调用次数以及循环的回边次数（多次执行的循环体）Profiling的C1编译

**3.完全C1编译代码：**我们Profiling里面所有的代码。也会被C1执行

**4.C2编译代码：**这个才是优化的级别。

> 级别越高，我们的应用启动越慢，优化下来开销会越高，同样的，我们的峰值性能也会越高

> 通常C2 代码的执行效率要比 C1 代码的高出 30% 以上

![image-20240706083323156](https://gitee.com/zhf19970510/image-server/raw/master/img_2024/20240706083323.png)

> Java 8 默认开启了分层编译。-XX:+TieredCompilation开启参数
>
> 不管是开启还是关闭分层编译，原本用来选择即时编译器的参数 **-client** 和 **-server** 都是无效的。当关闭分层编译的情况下，Java 虚拟机将直接采用 C2。
>
> 如果你希望只是用 C1，那么你可以在打开分层编译的情况下使用参数 **-XX:TieredStopAtLevel=1**。在这种情况下，Java 虚拟机会在解释执行之后直接由 1 层的 C1 进行编译。

#### 热点代码：

在运行过程中会被即时编译的“热点代码” 有两类，即：

* **被多次调用的方法**
* **被多次执行的循环体**

对于第一种，编译器会将整个方法作为编译对象，这也是标准的JIT 编译方式。对于第二种是由循环体出发的，但是编译器依然会以整个方法（而不是单独的循环体）作为编译对象，因为发生在方法执行过程中，称为栈上替换（On Stack Replacement，简称为 OSR 编译，即方法栈帧还在栈上，方法就被替换了）。

#### 如何找到热点代码？

判断一段代码是否是热点代码，是不是需要触发即时编译，这样的行为称为热点探测（Hot Spot Detection），探测算法有两种，分别如下：

* **基于采样的热点探测（Sample Based Hot Spot Detection）：**虚拟机会周期的对各个线程栈顶进行检查，如果某些方法经常出现在栈顶，这个方法就是“热点方法”。好处是实现简单、高效，很容易获取方法调用关系。缺点是很难确认方法的 reduce，容易受到线程阻塞或其他外因扰乱。
* **基于计数器的热点探测（Counter Based Hot Spot Detection）**：为每个方法（甚至是代码块）建立计数器，执行次数超过阈值就认为是“热点方法”。优点是统计结果精确严谨。缺点是实现麻烦，不能直接获取方法的调用关系。

HotSpot 使用的是第二种——基于计数器的热点探测，并且有两类计数器：方法调用计数器（Invocation Counter ）和回边计数器（Back Edge Counter ）。

这两个计数器都有一个确定的阈值，超过后便会触发 JIT 编译。



### java两大计数器：

(1)**首先是方法调用计数器** 。Client 模式下默认阈值是 **1500** 次，在 Server 模式下是 **10000**次，这个阈值可以通过 **-XX：CompileThreadhold** 来人为设定。如果不做任何设置，方法调用计数器统计的并不是方法被调用的绝对次数，而是一个相对的执行频率，即一段时间之内的方法被调用的次数。当超过一定的时间限度，如果方法的调用次数仍然不足以让它提交给即时编译器编译，那么这个方法的调用计数器就会被减少一半，这个过程称为方法调用计数器热度的衰减（Counter Decay），而这段时间就成为此方法的统计的半衰周期（ Counter Half Life Time）。进行热度衰减的动作是在虚拟机进行垃圾收集时顺便进行的，可以使用虚拟机参数 **-XX：CounterHalfLifeTime** 参数设置半衰周期的时间，单位是秒。整个 JIT 编译的交互过程如下图。

![image-20240707055905039](https://gitee.com/zhf19970510/image-server/raw/master/img_2024/20240707055905.png)

**（2）第二个回边计数器** ，作用是统计一个方法中循环体代码执行的次数，在字节码中遇到控制流向后跳转的指令称为“回边”（ Back Edge ）。显然，建立回边计数器统计的目的就是为了触发 OSR 编译。

![image-20240707063027519](https://gitee.com/zhf19970510/image-server/raw/master/img_2024/20240707063027.png)

关于这个计数器的阈值， HotSpot 提供了 **-XX：BackEdgeThreshold** 供用户设置，但是当前的虚拟机实际上使用了 **-XX：OnStackReplacePercentage** 来简介调整阈值，计算公式如下：

* 在 **Client** 模式下， 公式为 方法调用计数器阈值（CompileThreshold）X **OSR 比率**（OnStackReplacePercentage）/ **100** 。其中 OSR 比率默认为 **933**，那么，回边计数器的阈值为 **13995**。
* 在 **Server** 模式下，公式为 方法调用计数器阈值（Compile Threashold）X （**OSR 比率**(OnStackReplacePercentage) - 解释器监控比率（InterpreterProfilePercent））/**100**。
  其中 onStackReplacePercentage 默认值为 **140**，InterpreterProfilePercentage 默认值为 **33**，如果都取默认值，那么 Server 模式虚拟机回边计数器阈值为 **10700** 。

与方法计数器不同，回边计数器没有计数热度衰减的过程，因此这个计数器统计的就是该方法循环执行的绝对次数。当计数器溢出的时候，它还会把方法计数器的值也调整到溢出状态，这样下次再进入该方法的时候就会执行标准编译过程。

可以看到，决定一个方法是否为热点代码的因素有两个：方法的调用次数、循环回边的执行次数。即时编译便是根据这两个计数器的和来触发的。为什么 Java 虚拟机需要维护两个不同的计数器呢？



#### **Code Cache**

JVM生成的native code存放的内存空间称之为Code Cache；JIT编译、JNI等都会编译代码到native code，其中JIT生成的native code占用了Code Cache的绝大部分空间，他是属于非堆内存的。



# 运行时优化

## 方法内联

方法内联，是指 **JVM在运行时将调用次数达到一定阈值的方法调用替换为方法体本身** ，从而消除调用成本，并为接下来进一步的代码性能优化提供基础，是JVM的一个重要优化手段之一。

简单通俗的讲就是把方法内部调用的其它方法的逻辑，嵌入到自身的方法中去，变成自身的一部分，之后不再调用该方法，从而节省调用函数带来的额外开支。

之所以出现方法内联是因为（方法调用）函数调用除了执行自身逻辑的开销外，还有一些不为人知的额外开销。 **这部分额外的开销主要来自方法栈帧的生成、参数字段的压入、栈帧的弹出、还有指令执行地址的跳转** 。

**很可能自身执行逻辑的开销还比不上为了调用这个方法的额外开锁。如果类似的方法被频繁的调用，则真正相对执行效率就会很低，虽然这类方法的执行时间很短。这也是为什么jvm会在热点代码中执行方法内联的原因，这样的话就可以省去调用调用函数带来的额外开支。**

### 内联条件

一个方法如果满足以下条件就很可能被jvm内联。

* 热点代码。 如果一个方法的执行频率很高就表示优化的潜在价值就越大。那代码执行多少次才能确定为热点代码？这是根据编译器的编译模式来决定的。如果是客户端编译模式则次数是1500，服务端编译模式是10000。次数的大小可以通过-XX:CompileThreshold来调整。
* 方法体不能太大。jvm中被内联的方法会编译成机器码放在code cache中。如果方法体太大，则能缓存热点方法就少，反而会影响性能。热点方法小于325字节的时候，非热点代码35字节以下才会使用这种方式
* 如果希望方法被内联， **尽量用private、static、final修饰** ，这样jvm可以直接内联。如果是public、protected修饰方法jvm则需要进行类型判断，因为这些方法可以被子类继承和覆盖，jvm需要判断内联究竟内联是父类还是其中某个子类的方法。

所以了解jvm方法内联机制之后，会有助于我们工作中写出能让jvm更容易优化的代码，有助于提升程序的性能。



## 逃逸分析

### 什么是“对象逃逸”？

对象逃逸的本质是对象指针的逃逸。

在计算机语言编译器优化原理中，逃逸分析是指分析指针动态范围的方法。

当变量（或者对象）在方法中分配后，其指针有可能被返回或者被全局引用，这样就会被其他方法或者线程所引用，这种现象称作指针（或者引用）的逃逸(Escape)。通俗点讲，如果一个对象的指针被多个方法或者线程引用时，那么我们就称这个对象的指针（或对象）的逃逸（Escape）。
### **什么是逃逸分析？**

逃逸分析，是一种可以有效减少Java 程序中同步负载和内存堆分配压力的跨函数全局数据流分析算法。通过逃逸分析，Java Hotspot编译器能够分析出一个新的对象的引用的使用范围从而决定是否要将这个对象分配到堆上。 逃逸分析（Escape Analysis）算是目前Java虚拟机中比较前沿的优化技术了。

注意：逃逸分析不是直接的优化手段，而是代码分析手段。



### 基于逃逸分析的优化

当判断出对象不发生逃逸时，编译器可以使用逃逸分析的结果作一些代码优化

* 栈上分配：将堆分配转化为栈分配。如果某个对象在子程序中被分配，并且指向该对象的指针永远不会逃逸，该对象就可以在分配在栈上，而不是在堆上。在的垃圾收集的语言中，这种优化可以降低垃圾收集器运行的频率。
* 同步消除：如果发现某个对象只能从一个线程可访问，那么在这个对象上的操作可以不需要同步。
* 分离对象或标量替换。如果某个对象的访问方式不要求该对象是一个连续的内存结构，那么对象的部分（或全部）可以不存储在内存，而是存储在CPU寄存器中。

### 标量替换

**标量：**不可被进一步分解的量，而JAVA的基本数据类型就是标量（比如int，long等基本数据类型） 。

**聚合量：** 标量的对立就是可以被进一步分解的量，称之为聚合量。 在JAVA中对象就是可以被进一步分解的聚合量。

**标量替换：**通过逃逸分析确定该对象不会被外部访问，并且对象可以被进一步分解时，JVM不会创建该对象，而是将该对象成员变量分解若干个被这个方法使用的成员变量所代替，这些代替的成员变量在栈帧或寄存器上分配空间，这样就不会因为没有一大块连续空间导致对象内存不够分配。

### 同步锁消除

**如果发现某个对象只能从一个线程可访问，那么在这个对象上的操作可以不需要同步** 。

### 什么条件下会触发逃逸分析？

对象会先尝试栈上分配，如果不能成功分配，那么就去TLAB，如果还不行，就判定当前的垃圾收集器悲观策略，可不可以直接进入老年代，最后才会进入Eden。

![image-20240708212830861](C:/Users/admin/AppData/Roaming/Typora/typora-user-images/image-20240708212830861.png)

Java的逃逸分析只发在JIT的即时编译中，因为在启动前已经通过各种条件判断出来是否满足逃逸，通过上面的流程图也可以得知对象分配不一定在堆上，所以可知满足逃逸的条件如下，只要满足以下任何一种都会判断为逃逸。

一、对象被赋值给堆中对象的字段和类的静态变量。
二、对象被传进了不确定的代码中去运行。

对象逃逸的范围有：全局逃逸、参数逃逸、没有逃逸;

### TLAB分配的对象可以共享吗？

答：只要是Heap上的对象，所有线程都是可以共享的，就看你有没有本事访问到了。在GC的时候只从root sets来扫描对象，而不管你到底在哪个TLAB中。

![image-20240708225844871](C:/Users/admin/AppData/Roaming/Typora/typora-user-images/image-20240708225844871.png)



## **性能监控工具**

### jconsole

**JConsole工具是JDK自带的图形化性能监控工具。并通过JConsole工具， 可以查看Java应用程序的运行概况， 监控堆信息、 元空间使用情况及类的加载情况等。**

JConsole程序在%JAVA_HOM E%/bin目录下

或者你可以直接在命令行对他进行打印

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/1463/1656048868088/12597031b6e44327b0c12ac06244244a.png)

##### 远程连接：

1. 设置被监控的Java虚拟机启动的參数，一般的情况下，会有下面三个參数，各自是：

-Dcom.sun.management.jmxremote.port=1090

-Dcom.sun.management.jmxremote.ssl=false

-Dcom.sun.management.jmxremote.authenticate=false

也就是说，你需要在启动参数后面加上这几个参数.

然后，我们输入要被监控的Java虚拟机的IP地址和port号，如果输入正确，连接button就上生效如果设计的监控port号为8082，连接的IP为：10.20.618.11（这个需要你防火墙以及端口都处于开放状态），例如以下图所看到的：

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/1463/1656048868088/b2f94d65785a4acfbc85d5535889fbab.png)

点击连接后，就会进入到正常的显示界面，说明就连接成功了。

#### JConsole的显示界面：

![image.png](https://fynotefile.oss-cn-zhangjiakou.aliyuncs.com/fynote/fyfile/1463/1656048868088/4f1e3fa6d8994f3892000fc84208b20f.png)   