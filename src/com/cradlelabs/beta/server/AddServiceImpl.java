package com.cradlelabs.beta.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.cradlelabs.beta.client.AddService;
import com.cradlelabs.beta.server.utils.DBConnection;
import com.cradlelabs.beta.server.utils.Utils;
import com.cradlelabs.beta.shared.AgentModel;
import com.cradlelabs.beta.shared.PinkSheetModel;
import com.cradlelabs.beta.shared.UserModel;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class AddServiceImpl extends RemoteServiceServlet implements AddService{
	private static Connection con = DBConnection.getConnection();

	@Override
	public int addAgent(AgentModel model) {
		PreparedStatement prstmt = null;
		con = DBConnection.getConnection();

		try{
			prstmt = (PreparedStatement) con.prepareStatement("insert into web_agents (name,msisdn,email,blob_key,poll_id,cons_id,year) values (?,?,?,?,?,?,?) ");
			prstmt.setString(1, Utils.getCapitalizedWord(model.getName()));
			prstmt.setString(2, model.getMsisdn());
			prstmt.setString(3, model.getEmail());
			prstmt.setString(4, model.getBlobKey());
			prstmt.setInt(5, model.getPollingStationId());
			prstmt.setInt(6, model.getConstituencyId());
			prstmt.setString(7, model.getYear());

			int success = prstmt.executeUpdate();
			if(success > 0){
				return addToPsUsers(model.getMsisdn(), model.getEmail(), Utils.getSHA256(model.getMsisdn()), "A");
			}
		}catch(SQLException sql){
			sql.printStackTrace();
		}
		return 0;
	}
	
	private int addToPsUsers(String msisdn, String email, String password, String type){
		PreparedStatement prstmt = null;
		con = DBConnection.getConnection();

		try{
			prstmt = (PreparedStatement) con.prepareStatement("insert into ps_users (msisdn, email,password,type) values (?,?,?,?) ");
			prstmt.setString(1, msisdn);
			prstmt.setString(2, email);
			prstmt.setString(3, password);
			prstmt.setString(4, type);

			int success = prstmt.executeUpdate();
			if(success > 0){
				return success;
			}
		}catch(SQLException sql){
			sql.printStackTrace();
		}
		return 0;
	}

	@Override
	public int addUser(UserModel model) {
		PreparedStatement prstmt = null;
		con = DBConnection.getConnection();

		try{
			prstmt = (PreparedStatement) con.prepareStatement("insert into web_users (name,msisdn,email,blob_key,perm) values (?,?,?,?,?) ");
			prstmt.setString(1, Utils.getCapitalizedWord(model.getName()));
			prstmt.setString(2, model.getMsisdn());
			prstmt.setString(3, model.getEmail());
			prstmt.setString(4, model.getBlobKey());
			prstmt.setString(5, model.getPerm());

			int success = prstmt.executeUpdate();
			if(success > 0){
				return addToPsUsers(model.getMsisdn(), model.getEmail(), Utils.getSHA256(model.getMsisdn()), "U");
			}
		}catch(SQLException sql){
			sql.printStackTrace();
		}
		return 0;
	}

	@Override
	public int addPinkSheet(PinkSheetModel model) {
		PreparedStatement prstmt = null;
		con = DBConnection.getConnection();

		try{
			prstmt = (PreparedStatement) con.prepareStatement("insert into pinksheets (agent_name,msisdn,email,blob_key,year,created_ts,poll_id,cons_id,constituency,pollingstation,status) values (?,?,?,?,?,?,?,?,?,?,?) ");
			prstmt.setString(1, Utils.getCapitalizedWord(model.getAgentName()));
			prstmt.setString(2, model.getMsisdn());
			prstmt.setString(3, model.getEmail());
			prstmt.setString(4, model.getBlobKey());
			prstmt.setString(5, model.getYear());
			prstmt.setString(6, Utils.getTodayDateTime());
			prstmt.setInt(7, model.getPollId());
			prstmt.setInt(8, model.getConstituencyId());
			prstmt.setString(9, model.getConstituency());
			prstmt.setString(10, model.getPollingStation());
			prstmt.setString(11, "A");

			int success = prstmt.executeUpdate();
			if(success > 0){
				return success;
			}
		}catch(SQLException sql){
			sql.printStackTrace();
		}
		return 0;
	}

}
