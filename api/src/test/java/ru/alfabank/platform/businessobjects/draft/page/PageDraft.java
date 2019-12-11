package ru.alfabank.platform.businessobjects.draft.page;

import com.fasterxml.jackson.annotation.*;
import ru.alfabank.platform.businessobjects.*;

import java.util.*;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PageDraft {

	private List<Operations> operations;
	private String version;

	public PageDraft(
			List<Operations> operations,
			String version) {
		this.operations = operations;
		this.version = version;
	}

	@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
	@JsonInclude(JsonInclude.Include.NON_NULL)
	public static class Operations {

		private Data data;
		private Entity entity;
		private Method method;
		private String uid;

		public Operations(
				Data data,
				Entity entity,
				Method method,
				String uid) {
			this.data = data;
			this.entity = entity;
			this.method = method;
			this.uid = uid;
		}
			@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
			@JsonInclude(JsonInclude.Include.NON_NULL)
			public static class Data {

				private String[] cityGroups;
				private Date dateFrom;
				private Date dateTo;
				private Device device;
				private boolean enable;
				private boolean hasChildren;
				private String localization;
				private String name;
				private String propertyUid;
				private Object value;


				public Data(
						String[] cityGroups,
						Date dateFrom,
						Date dateTo,
						Device device,
						boolean enable,
						boolean hasChildren,
						String localization,
						String name) {
					this.cityGroups = cityGroups;
					this.dateFrom = dateFrom;
					this.dateTo = dateTo;
					this.device = device;
					this.enable = enable;
					this.hasChildren = hasChildren;
					this.localization = localization;
					this.name = name;
				}
				public Data(String propertyUid, Object value, String[] cityGroups) {
					this.propertyUid = propertyUid;
					this.value = value;
					this.cityGroups = cityGroups;
				}
		}
	}
}
