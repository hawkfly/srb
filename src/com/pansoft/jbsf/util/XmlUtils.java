package com.pansoft.jbsf.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.Node;
import org.dom4j.QName;
import org.dom4j.XPath;

public class XmlUtils {
	private Document doc = null;
	private Element root = null;
	private static XmlUtils xmlUtils;

	private XmlUtils(String xml){
		try {
			doc = DocumentHelper.parseText(xml);
			root = doc.getRootElement();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}
	
	private void reoload(String xml)throws Exception{
		doc = DocumentHelper.parseText(xml);
		root = doc.getRootElement();
	}
	
	public static XmlUtils newInstance(String xml) throws Exception{
		// SAXReader reader = new SAXReader();
		if(xmlUtils == null)xmlUtils = new XmlUtils(xml);
		else xmlUtils.reoload(xml);
		return xmlUtils;
	}

	/**
	 * 获取所有名称为nodeName的节点
	 * 
	 * @param nodeName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Node> getNodeList(String startnodeName, String path, String... namespaces) {
		if(namespaces !=null && path != null){
			XPath x = doc.createXPath("//"+startnodeName);
			Map<String, String> map = new HashMap<String, String>();
			for(int i = 0; i < namespaces.length; i++){
				String np[] = namespaces[i].split("=");
				map.put(np[0], np[1]);
			}
			x.setNamespaceURIs(map);
			List<Node> nodes =  x.selectNodes(doc);
			List<Node> allnodes = new ArrayList<Node>();
			for(int i = 0 ; i < nodes.size(); i++){
				XPath xi = nodes.get(i).createXPath(path);
				xi.setNamespaceURIs(map);
				List<Node> inodes = xi.selectNodes(nodes.get(i));
				allnodes.addAll(inodes);
			}
			return allnodes;
		}else{
			return root.selectNodes("//" + startnodeName);
		}
	}
	
	public String getAttributeValue(Node node, String attrname, String namespace){
		if(namespace == null)return null;
		String[] np = namespace.split("=");
		return ((Element)node).attributeValue(new QName(attrname, new Namespace(np[0], np[1])));
	}

	/**
	 * 获取名称为nodeName的文本
	 * 
	 * @param nodeName
	 * @return
	 */
	public Node getNodeText(String nodeName, String... namespaces) {
		Node n = null;
		if(namespaces !=null){
			XPath x = doc.createXPath("//"+nodeName);
			Map<String, String> map = new HashMap<String, String>();
			for(int i = 0; i < namespaces.length; i++){
				String np[] = namespaces[i].split("=");
				map.put(np[0], np[1]);
			}
			x.setNamespaceURIs(map);
			n = x.selectSingleNode(doc);
		}else{
			n = root.selectSingleNode("//" + nodeName);
		}
		
		return n;
	}

	/**
	 * 获取节点名称为nodeName的attrName属性
	 * 
	 * @param nodeName
	 * @param attrName
	 * @return
	 */
	public String getNodeAttribute(String nodeName, String attrName) {
		Node node = root.selectSingleNode("//" + nodeName);
		return node.valueOf("@" + attrName);
	}

	/**
	 * 获取节点名称为nodeName的attrName属性
	 * 
	 * @param nodeName
	 * @param attrName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String getNodeAttribute(String nodeName, String attrName, int index) {
		List<Node> nodes = root.selectNodes("//" + nodeName + "[@" + attrName+ "]");
		return nodes.get(index).valueOf("@" + attrName);
	}

	/**
	 * 获取节点名称为nodeName的attrName属性 条件：改节点里面有个属性为key值为value的属性
	 * 
	 * @param nodeName
	 * @param attrName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String getNodeAttribute(String nodeName, String attrName, String key, String value) {
		List<Node> nodes = root.selectNodes("//" + nodeName + "[@" + key + "='"+ value + "']");
		return nodes.get(0).valueOf("@" + attrName);
	}
}
