package br.com.hfs.admin.service;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.hibernate.Session;
import org.hibernate.TransactionException;
import org.hibernate.jdbc.ReturningWork;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.hfs.admin.controller.dto.AdmUserDTO;
import br.com.hfs.admin.model.AdmProfile;
import br.com.hfs.admin.model.AdmUser;
import br.com.hfs.admin.repository.AdmUserRepository;
import br.com.hfs.base.BaseService;
import br.com.hfs.util.BaseUtil;
import br.com.hfs.util.bcrypt.BCryptUtil;
import jakarta.transaction.Transactional;

@Service
public class AdmUserService extends BaseService<AdmUser, Long, AdmUserRepository> {

	private static final long serialVersionUID = 1L;

	@Autowired
	private AdmProfileService admProfileService;
	
	public AdmUserService() {
		super(AdmUser.class);
	}
	
	private AdmUser setTransient(AdmUser item) {
		List<Long> admIdProfiles = new ArrayList<Long>();
		List<AdmProfile> admProfiles = admProfileService.findProfilesByUser(item.getId());
		
		admProfiles.forEach(profile -> admIdProfiles.add(profile.getId()));
		item.setAdmIdProfiles(admIdProfiles);
		
		List<String> listUserProfiles = new ArrayList<String>();
		admProfiles.forEach(profile -> listUserProfiles.add(profile.getDescription()));
		item.setUserProfiles(listUserProfiles.stream().collect(Collectors.joining(",")));	
		
		return item;
	}
	
	@Override
	public Optional<AdmUser> findById(Long id) {
		Optional<AdmUser> item = repository.findById(id);
		if (item.isPresent()) {
			AdmUser newItem = setTransient(item.get());
			return Optional.of(newItem);
		} else {
			return Optional.empty();
		}
	}
	
	private List<AdmUser> setTransient(List<AdmUser> lista) {
		lista.stream().forEach(item -> setTransient(item));
		return lista;
	}

	@Override
	public List<AdmUser> findAll() {
		List<AdmUser> lista = repository.findAll();		
		return setTransient(lista);
	}
	
	private Page<AdmUser> setTransient(Page<AdmUser> lista) {
		lista.stream().forEach(item -> setTransient(item));
		return lista;
	}

	@Override
	public Page<AdmUser> findAll(Pageable pageable) {
		Page<AdmUser> lista = repository.findAll(pageable);
		return setTransient(lista);
	}

	public Page<AdmUser> findByNameLike(String name, Pageable pagination){
		return repository.findByNameLike(name, pagination);
	}
	
	public List<AdmUser> findByNameLike(String name){
		return repository.findByNameLike(name);
	}
	
	public Optional<AdmUser> findByName(String name) {
		return repository.findByName(name);
	}

	public Optional<AdmUser> findByEmail(String email){
		return repository.findByEmail(email);
	}
	
	private List<AdmUserDTO> toDTO(List<AdmUser> listaOrigem) {
		ModelMapper modelMapper = new ModelMapper();		
		AdmUserDTO userDTO; 
		List<AdmUserDTO> lista = new ArrayList<AdmUserDTO>(); 
		for (AdmUser item : listaOrigem) {
			userDTO = modelMapper.map(item, AdmUserDTO.class);
			lista.add(userDTO);
		}
		return lista;
	}
	
	public List<AdmUserDTO> findByLikeEmail(String email){
		List<AdmUser> listaOrigem = repository.findByLikeEmail(email);
		List<AdmUserDTO> lista = toDTO(listaOrigem);
		
		return lista;
	}	
	
	public List<AdmUser> findPaginated(int pageNumber, int pageSize) {
		return findPaginated("ADM_USER", "USU_SEQ", pageNumber, pageSize);
	}

	public List<AdmUser> listByRange(int startInterval, int endInterval) {
		return listByRange("ADM_USER", "USU_SEQ", startInterval, endInterval);
	}

	public String findIPByOracle() {
		List<Object> ip = repository.findIPByOracle();
		return ip.get(0).toString();
	}
	
	public String findIPByPostgreSQL() {
		List<Object> ip = repository.findIPByPostgreSQL();
		return ip.get(0).toString();
	}
	
	public String setLoginPostgreSQL(String login) {
		List<Object> lista = repository.setLoginPostgreSQL(login);
		return lista.get(0).toString();
	}
	
	public String setIPPostgreSQL(String ip) {
		List<Object> lista = repository.setIPPostgreSQL(ip);
		return lista.get(0).toString();
	}
	
	public String findBanco() throws TransactionException {
		String retorno = "";
		final SQLException[] erro = new SQLException[1];

		if (em.isOpen()) {

			Session session = em.unwrap(Session.class);

			retorno = session.doReturningWork(new ReturningWork<String>() {
				@Override
				public String execute(Connection connection) {
					try {
						if (!connection.isClosed()) {
							DatabaseMetaData dbmd = connection.getMetaData();

							if (dbmd.getDriverName().indexOf("Oracle") != -1) {
								return "Oracle";
							}
							if (dbmd.getDriverName().indexOf("PostgreSQL") != -1) {
								return "PostgreSQL";
							}
							if (dbmd.getDriverName().indexOf("mysql") != -1) {
								return "MySQL";
							}
						}
					} catch (SQLException e) {
						erro[0] = e;
						return e.getMessage();
					}
					return "";
				}
			});

		}

		if (erro.length > 0) {
			if (erro[0] != null) {
				if (!erro[0].getMessage().isEmpty()) {
					throw new TransactionException(erro[0].getMessage(), erro[0]);
				}
			}
		}

		return retorno;
	}
	
	public boolean setOracleLoginAndIP(String login, String ip) throws TransactionException {
		Integer retorno = -1;
		final SQLException[] erro = new SQLException[1];

		if (em.isOpen()) {

			Session session = em.unwrap(Session.class);

			retorno = session.doReturningWork(new ReturningWork<Integer>() {
				@Override
				public Integer execute(Connection connection) {
					try {
						if (!connection.isClosed()) {
							CallableStatement call = connection.prepareCall("{ call pkg_adm.setar_usuario_ip(?, ?) }");
							call.setString(1, login);
							call.setString(2, ip);
							call.executeUpdate();
						}
					} catch (SQLException e) {
						erro[0] = e;
						return -1;
					}
					return 0;
				}
			});

		}

		if (erro.length > 0) {
			if (erro[0] != null) {
				if (!erro[0].getMessage().isEmpty()) {
					throw new TransactionException(erro[0].getMessage(), erro[0]);
				}
			}
		}

		return (retorno == 0);
	}

	public Optional<AdmUser> findByLogin(String login) {
		Optional<AdmUser> item = repository.findByLogin(login);
		if (item.isPresent()) {
			AdmUser newItem = setTransient(item.get());
			return Optional.of(newItem);
		} else {
			return Optional.empty();
		}
	}
	
	
	@Transactional
	public AdmUser getUser(String login, String name, String email, boolean auditar) throws TransactionException {
		AdmUser user = null;
		if (user == null) {
			user = new AdmUser();
			user.setId(null);
			user.setLogin(login);
			user.setName(name);
			user.setEmail(email);
			//user.setIp();
			
			if (auditar) {
				this.insert(user);
			}
		}
		
		/*
		if (auditar) {
			String banco = findBanco();
			if (banco.equals("Oracle")) {
				setOracleLoginAndIP(user.getLogin().toString(), user.getIp());
			}
			if (banco.equals("PostgreSQL")) {
				setLoginPostgreSQL(user.getLogin().toString());
				setIPPostgreSQL(user.getIp());
			}
		}
		*/

		return user;
	}
	

	/*
		As minimum requirements for user passwords, the following should be considered:
			Minimum length of 8 characters;
			Presence of at least 3 of the 4 character classes below:
				uppercase characters;
				lowercase characters;
				numbers;
				special characters;
				Absence of strings (eg: 1234) or consecutive identical characters (yyyy);
				Absence of any username identifier, such as: John Silva - user: john.silva - password cannot contain "john" or "silva".
	 */
	public boolean validarSenha(String login, String senha){
		if (senha.length() >= 8) {
			Pattern letterUppercase = Pattern.compile("[A-Z]");
			Pattern letterLowercase = Pattern.compile("[a-z]");
			Pattern digit = Pattern.compile("[0-9]");
			Pattern special = Pattern.compile("[!@#$%&*()_+=|<>?{}\\[\\]~-]");
	
			Matcher hasLetterUppercase = letterUppercase.matcher(senha);
			Matcher hasLetterLowercase = letterLowercase.matcher(senha);
			Matcher hasDigit = digit.matcher(senha);
			Matcher hasSpecial = special.matcher(senha);			
						
			boolean U = hasLetterUppercase.find();
			boolean L = hasLetterLowercase.find();
			boolean D = hasDigit.find();
			boolean S = hasSpecial.find();
			
			boolean hasChars = (U && L && D) || (S && U && L) || (D && S && U) || (L && D && S);
			
			return hasChars 
					&& !BaseUtil.containsNumericSequences(4,9, senha) 
					&& !BaseUtil.containsConsecutiveIdenticalCharacters(4,9, senha)
					&& !senha.contains(login);
	
		} else
			return false;
	}

	@Transactional
	public boolean updatePassword(AdmUser admUser) throws TransactionException {		
		try {

			//PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
			//String hashed = encoder.encode(admUser.getConfirmNewPassword());
			
			String hashed = BCryptUtil.hash(admUser.getConfirmNewPassword());
			admUser.setPassword(hashed);

	        admUser = repository.save(admUser);
	        
	        return admUser.getId()!=null;
			
		} catch (Exception e) {
			throw new TransactionException(e.getMessage(), e);
		}
		//return false;
	}
}
