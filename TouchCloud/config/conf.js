var ioc = {
	dataSource : {  
        type : "org.apache.commons.dbcp.BasicDataSource",  
        fields : {  
        	driverClassName : "com.mysql.jdbc.Driver",  
            url : "jdbc:mysql://localhost/touchcloud",  
            username : "root",  
            password : "root"  
        }   
    },
    dao : {
        type : "org.nutz.dao.impl.NutDao",
        args : [{refer:"dataSource"}]
    },
    timer : {
    	type:"com.touchCloud.timer.TimerData"
    }
}