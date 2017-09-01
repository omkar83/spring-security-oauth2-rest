package org.ateam.common.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
public class OauthRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private final static String FETCH_BY_USERNAME = "select username,password from auth_details where username=?";

	private final static String FETCH_BY_CLIENT_ID = "select client_id, client_secret, resource_ids, scope, authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, refresh_token_validity, additional_information, autoapprove from oauth_client_details where client_id = ?";
	
	private final static String FETCH_BY_ANNUITY_INFO = "select POLICY_NO, NENKIN_SHOSHO_NO, HOLDER_NAME, HOLDER_KNAME, HOLDER_SEX, HOLDER_BIRTHDAY, HOLDER_ZIP_CODE, HOLDER_ADDRESS1, HOLDER_ADDRESS2, HOLDER_ADDRESS3, HOLDER_ADDRESS4, HOLDER_TEL, HOLDER_OR_CORP_KBN, HOLDER_ANOTHER_ZIP_CODE, HOLDER_ANOTHER_TEL, HOLDER_ANOTHER_ADDRESS1, HOLDER_ANOTHER_ADDRESS2, HOLDER_ANOTHER_ADDRESS3, HOLDER_ANOTHER_ADDRESS4, INSURED_NAME, INSURED_KNAME, INSURED_SEX, INSURED_BIRTHDAY, INSURED_ZOKUGARA, INSURED_OR_CORP_KBN, INSUREDE_KEIYAKUZI_NENREI, ANNUITANT_NAME, ANNUITANT_KNAME, ANNUITANT_SEX, ANNUITANT_BIRTHDAY, ANNUITANT_ZIP_CODE, ANNUITANT_ADDRESS1, ANNUITANT_ADDRESS2, ANNUITANT_ADDRESS3, ANNUITANT_ADDRESS4, ANNUITANT_TEL, ANNUITANT_ZOKUGARA, ANNUITANT_ANOTHER_ZIP_CODE, ANNUITANT_ANOTHER_ADDRESS1, ANNUITANT_ANOTHER_ADDRESS2, ANNUITANT_ANOTHER_ADDRESS3, ANNUITANT_ANOTHER_ADDRESS4, ANNUITANT_OR_CORP_KBN, KAZEI_KBN, ANNUITANT_STAKEHOLDER_KBN, CONTINGENT_ANNUITANT_NAME, CONTINGENT_ANNUITANT_KNAME, CONTINGENT_ANNUITANT_SEX, CONTINGENT_ANNUITANT_BIRTHDAY, CON_ANNUITANT_ANOTHER_ZIP_CODE, CON_ANNUITANT_ANOTHER_ADDRESS1, CON_ANNUITANT_ANOTHER_ADDRESS2, CON_ANNUITANT_ANOTHER_ADDRESS3, CON_ANNUITANT_ANOTHER_ADDRESS4, CON_ANNUITANT_TEL, CON_ANNUITANT_ZOKUGARA, CON_ANNUITANT_STAKEHOLDER_KBN, HOSHO_CD, SHOHIN_CD, KATA_CD, KEIYAKU_STATUS, TOSHO_KEIYAKU_DATE, KEIYAKU_DATE, SEKININKAISHI_DATE, SHODAKU_DATE, NENKIN_SIHARAI_KAISI_DATE, NENKIN_SIHARAI_KAISI_NENREI, HENKO_NO_NENKIN_SHURUI, HENKO_NO_NENKIN_HOSHO_KIKAN, HENKO_NENKIN_SHIHARAI_KIKAN, EN_NYUKIN_TOKUYAKU_FUKA, NENKIN_BARAI_TOKUYAKU_FUKA, NENKIN_BARAI_TOKUYAKU_UMU, NENKIN_BARAI_TOKUYAKU_SHURUI, NENKIN_BARAI_TOKUYAKU_KIKAN, YAKKAN_CATEGORY_CD, YAKKAN_CD from annuity_info";

	public User getByUsername(String username) {
		return jdbcTemplate.queryForObject(FETCH_BY_USERNAME, new Object[] { username }, new RowMapper<User>() {
			@Override
			public User mapRow(ResultSet resultSet, int i) throws SQLException {
				Collection<GrantedAuthority> grantedAuths = new HashSet<GrantedAuthority>();
				grantedAuths.add(new SimpleGrantedAuthority("ROLE_USER"));
				User user = new User(resultSet.getString("username"), resultSet.getString("password"), grantedAuths);
				return user;
			}
		});
	}
	
	public List<Map<String, Object>> getByAnnuityInfo() {
		return jdbcTemplate.queryForList(FETCH_BY_ANNUITY_INFO);
	}

	public BaseClientDetails getByClientId(String clientId) {
		return jdbcTemplate.queryForObject(FETCH_BY_CLIENT_ID, new Object[] { clientId },
				new RowMapper<BaseClientDetails>() {
					@Override
					public BaseClientDetails mapRow(ResultSet rs, int i) throws SQLException {
						BaseClientDetails details = new BaseClientDetails(rs.getString(1), rs.getString(3),
								rs.getString(4), rs.getString(5), rs.getString(7), rs.getString(6));
						details.setClientSecret(rs.getString(2));
						if (rs.getObject(8) != null) {
							details.setAccessTokenValiditySeconds(Integer.valueOf(rs.getInt(8)));
						}

						if (rs.getObject(9) != null) {
							details.setRefreshTokenValiditySeconds(Integer.valueOf(rs.getInt(9)));
						}

						String scopes1 = rs.getString(11);
						if (scopes1 != null) {
							details.setAutoApproveScopes(StringUtils.commaDelimitedListToSet(scopes1));
						}
						return details;
					}
				});
	}

	/*
	 * private class CustomRole implements GrantedAuthority { private static
	 * final long serialVersionUID = 1L; String role = null;
	 * 
	 * @Override public String getAuthority() { return role; }
	 * 
	 * public void setAuthority(String roleName) { this.role = roleName; }
	 * 
	 * }
	 */

}
