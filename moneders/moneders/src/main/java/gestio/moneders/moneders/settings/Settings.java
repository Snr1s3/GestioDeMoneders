package gestio.moneders.moneders.settings;

import java.nio.file.Paths;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Settings {
	public String path = Paths.get("proba.xlsx").toString();
}