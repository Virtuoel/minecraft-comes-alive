package mca.enums;

import java.util.ArrayList;
import java.util.List;

import mca.core.MCA;
import radixcore.util.NumberCycleList;
import radixcore.util.RadixExcept;
import radixcore.util.RadixMath;

public enum EnumProfessionGroup 
{
	Farmer(0),
	Baker(0),
	Butcher(4),
	Guard(3),
	Child(0),
	Librarian(1),
	Miner(3),
	Priest(2),
	Smith(3),
	Warrior(3);

	private List<String> completeSkinList;
	private List<String> maleSkinList;
	private List<String> femaleSkinList;
	private int vanillaId;

	private EnumProfessionGroup(int vanillaId)
	{
		this.completeSkinList = new ArrayList<String>();
		this.maleSkinList = new ArrayList<String>();
		this.femaleSkinList = new ArrayList<String>();
		this.vanillaId = vanillaId;
	}

	public void addSkin(String locationInJAR)
	{
		String resourceLocation = locationInJAR.replace("/assets/mca/", "mca:");
		completeSkinList.add(resourceLocation);

		String genderChar = resourceLocation.replace("mca:textures/skins/" + this.toString(), "").substring(0, 1);

		if (genderChar.equals("M"))
		{
			maleSkinList.add(resourceLocation);
		}

		else if (genderChar.equals("F"))
		{
			femaleSkinList.add(resourceLocation);
		}
	}

	private String getSkin(boolean isMale)
	{
		List<String> skinList = isMale ? maleSkinList : femaleSkinList;

		try
		{
			return skinList.get(RadixMath.getNumberInRange(0, skinList.size() - 1));
		}

		catch (Exception e)
		{
			RadixExcept.logErrorCatch(e, "Unable to generate random skin for skin group <" + this.toString() + ">" + "!");
			return "";
		}
	}

	public List<String> getSkinList(boolean isMale)
	{
		return isMale ? maleSkinList : femaleSkinList;
	}

	public NumberCycleList getListOfSkinIDs(boolean isMale)
	{
		List<String> textureList = getSkinList(isMale);
		List<Integer> ids = new ArrayList<Integer>();

		for (String texture : textureList)
		{
			int id = Integer.parseInt(texture.replaceAll("[^\\d]", ""));
			ids.add(id);
		}

		return NumberCycleList.fromList(ids);
	}

	public String getMaleSkin()
	{
		return getSkin(true);
	}

	public String getFemaleSkin()
	{
		return getSkin(false);
	}

	private EnumProfessionGroup getRandomGroup(boolean excludeChild)
	{
		EnumProfessionGroup generatedGroup;
		boolean isValid = false;

		do
		{
			int index = RadixMath.getNumberInRange(0, EnumProfessionGroup.values().length - 1);
			generatedGroup = EnumProfessionGroup.values()[index];

			if (excludeChild && generatedGroup == Child)
			{
				continue;
			}

			else
			{
				isValid = true;
			}
		}
		while(!isValid);

		return generatedGroup;
	}

	public static void dumpSkinCounts()
	{
		MCA.getLog().info("Dumping skin counts...");

		for (EnumProfessionGroup group : EnumProfessionGroup.values())
		{
			MCA.getLog().info("Group <" + group.toString() + "> has " + group.completeSkinList.size() + " skins. " + group.maleSkinList.size() + " male and " + group.femaleSkinList.size() + " female.");
		}
	}

	public int getVanillaProfessionId() 
	{
		return vanillaId;
	}
}
