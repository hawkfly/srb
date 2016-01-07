package conf;

import java.io.IOException;
import java.text.ParseException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import testcase.TestController;

import com.jf.config.Constants;
import com.jf.config.Handlers;
import com.jf.config.Interceptors;
import com.jf.config.JbsfConfig;
import com.jf.config.Plugins;
import com.jf.config.Routes;
import com.jf.core.Jbsf;
import com.jf.plugin.activerecord.ActiveRecordPlugin;
import com.jf.plugin.activerecord.CaseInsensitiveContainerFactory;
import com.jf.plugin.activerecord.dialect.MysqlDialect;
import com.jf.plugin.spring.SpringPlugin;
import com.jf.render.FreeMarkerRender;
import com.jf.render.ViewType;
import com.pansoft.jbsf.context.SpringContext;
import com.pansoft.jbsf.controller.RegisterController;
import com.pansoft.jbsf.service.TimingTask.autoqs.QSQuartz;
import com.pansoft.jbsf.service.TimingTask.orderclear.OCQuartz;
import com.pansoft.util.Consts;
import com.shcm.SendmesController;

import freemarker.template.TemplateModelException;

/**
 * config jbsf framework
 * @author hawkfly
 */
public class JbsfIVConfig extends JbsfConfig {
	public static Properties property;
	Log log = LogFactory.getLog(JbsfIVConfig.class);
	@Override
	public void configConstant(Constants me) {
		// TODO Auto-generated method stub
		me.setDevMode(true);
		me.setBaseViewPath("/webresources/pages/");
		me.setViewType(ViewType.JSP);
		property = loadPropertyFile("/classes/conf/set.properties");
		try 
		{
		//	new UpdateTable().start();
			FreeMarkerRender.getConfiguration().setSharedVariable("basepath", Jbsf.me().getContextPath());
			OCQuartz.start();
			QSQuartz.start();
		} catch (TemplateModelException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void configHandler(Handlers me) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void configInterceptor(Interceptors me) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void configPlugin(Plugins me) {
		// TODO Auto-generated method stub
		//loadPropertyFile("/classes/conf/jdbc.properties");
		try {
			String path = SpringContext.getContext().getResource("/").getFile().getPath()+"/WEB-INF/applicationContext.xml";
			log.warn("path => " + path);
			me.add(new SpringPlugin("/"+path));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DataSource ds = (DataSource)SpringContext.getContext().getBean("dataSource");
		/*C3p0Plugin cp = new C3p0Plugin();
		cp.setDriverClass(getProperty("jdbc.driver"));
		me.add(cp);*/
		ActiveRecordPlugin arp = new ActiveRecordPlugin(ds, Consts.TRANSACTION_REPEATABLE_READ);
		me.add(arp);
		//arp.setDialect(new OracleDialect());
		arp.setDialect(new MysqlDialect());
		arp.setShowSql(true);
		arp.setContainerFactory(new CaseInsensitiveContainerFactory());
		//arp.addMapping("ZZSINVO", Invoice.class);
		
		//me.add(new CtfPlugin());//启动扫描认证插件
		//me.add(new AdapterPlugin());//启动扫描适配器
		
	}

	@Override
	public void configRoute(Routes me) {
		me.add("/jf/test", TestController.class);
		me.add("/jf/srb", RegisterController.class);
		me.add("/jf/sms", SendmesController.class);
	}

}
