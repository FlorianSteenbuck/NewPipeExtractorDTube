package org.schabi.newpipe.extractor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import static java.util.Arrays.asList;

public enum Locale {
    SAINT_BARTHÃ_LEMY(
            new String[]{"bl"}
    ),
    PORTUGAL(
            new String[]{"pt"}
    ),
    ERITREA(
            new String[]{"er"}
    ),
    UNITED_STATES(
            new String[]{"us"}
    ),
    MONTENEGRO(
            new String[]{"me"}
    ),
    EURASIAN_PATENT_ORGANIZATION(
            new String[]{"ev"}
    ),
    UZBEKISTAN(
            new String[]{"uz"}
    ),
    TOGO(
            new String[]{"tg"}
    ),
    CROATIA(
            new String[]{"hr"}
    ),
    CAMBODIA(
            new String[]{"kh"}
    ),
    KIRIBATI(
            new String[]{"ki"}
    ),
    EL_SALVADOR(
            new String[]{"sv"}
    ),
    ESWATINI(
            new String[]{"sz"}
    ),
    BARBADOS(
            new String[]{"bb"}
    ),
    BENIN(
            new String[]{"dy","bj"}
    ),
    HONG_KONG(
            new String[]{"hk"}
    ),
    ALGERIA(
            new String[]{"dz"}
    ),
    OMAN(
            new String[]{"om"}
    ),
    MEXICO(
            new String[]{"mx"}
    ),
    SENEGAL(
            new String[]{"sn"}
    ),
    ETHIOPIA(
            new String[]{"et"}
    ),
    SWEDEN(
            new String[]{"se"}
    ),
    RWANDA(
            new String[]{"rw"}
    ),
    NIUE(
            new String[]{"nu"}
    ),
    PERU(
            new String[]{"pe"}
    ),
    SVALBARD_AND_JAN_MAYEN(
            new String[]{"sj"}
    ),
    ANGUILLA(
            new String[]{"ai"}
    ),
    NORWAY(
            new String[]{"no"}
    ),
    REPUBLIC_OF_MACEDONIA(
            new String[]{"mk"}
    ),
    QATAR(
            new String[]{"qa"}
    ),
    REPUBLIC_OF_IRELAND(
            new String[]{"ie"}
    ),
    PALAU(
            new String[]{"pw"}
    ),
    RÃ_UNION(
            new String[]{"re"}
    ),
    CHRISTMAS_ISLAND(
            new String[]{"cx"}
    ),
    DENMARK(
            new String[]{"dk"}
    ),
    BURKINA_FASO(
            new String[]{"bf"}
    ),
    NEPAL(
            new String[]{"np"}
    ),
    AZERBAIJAN(
            new String[]{"az"}
    ),
    COOK_ISLANDS(
            new String[]{"ck"}
    ),
    ZIMBABWE(
            new String[]{"zw"}
    ),
    GABON(
            new String[]{"ga"}
    ),
    GIBRALTAR(
            new String[]{"gi"}
    ),
    VANUATU(
            new String[]{"vu"}
    ),
    YEMEN(
            new String[]{"ye"}
    ),
    VENEZUELA(
            new String[]{"yv","ve"}
    ),
    MOZAMBIQUE(
            new String[]{"mz"}
    ),
    ECUADOR(
            new String[]{"ec"}
    ),
    AMERICAN_SAMOA(
            new String[]{"as"}
    ),
    AUSTRALIA(
            new String[]{"au"}
    ),
    BOUVET_ISLAND(
            new String[]{"bv"}
    ),
    EAST_TIMOR(
            new String[]{"tp","tl"}
    ),
    WESTERN_SAHARA(
            new String[]{"eh"}
    ),
    KUWAIT(
            new String[]{"kw"}
    ),
    TANZANIA(
            new String[]{"tz"}
    ),
    FRENCH_SOUTHERN_AND_ANTARCTIC_LANDS(
            new String[]{"tf"}
    ),
    COCOS__KEELING__ISLANDS(
            new String[]{"cc"}
    ),
    FRANCE(
            new String[]{"fr"}
    ),
    TOKELAU(
            new String[]{"tk"}
    ),
    ARMENIA(
            new String[]{"am"}
    ),
    AUSTRIA(
            new String[]{"at"}
    ),
    ARUBA(
            new String[]{"aw"}
    ),
    NAMIBIA(
            new String[]{"na"}
    ),
    UNITED_STATES_MINOR_OUTLYING_ISLANDS(
            new String[]{"um"}
    ),
    COMOROS(
            new String[]{"km"}
    ),
    CANARY_ISLANDS(
            new String[]{"ic"}
    ),
    TONGA(
            new String[]{"to"}
    ),
    SAINT_PIERRE_AND_MIQUELON(
            new String[]{"pm"}
    ),
    SAINT_KITTS_AND_NEVIS(
            new String[]{"kn"}
    ),
    LIBYA(
            new String[]{"ly"}
    ),
    UNITED_STATES_VIRGIN_ISLANDS(
            new String[]{"vi"}
    ),
    MAYOTTE(
            new String[]{"yt"}
    ),
    FRENCH_GUIANA(
            new String[]{"gf"}
    ),
    NORTH_KOREA(
            new String[]{"kp"}
    ),
    ANTARCTICA(
            new String[]{"aq"}
    ),
    VIETNAM(
            new String[]{"vn"}
    ),
    EQUATORIAL_GUINEA(
            new String[]{"gq"}
    ),
    EU_PATENT_1970S_AND_1980S__PROPOSED_COMMUNITY_PATENT_CONVENTION(
            new String[]{"ef"}
    ),
    FRENCH_POLYNESIA(
            new String[]{"pf"}
    ),
    SWITZERLAND(
            new String[]{"ch"}
    ),
    SOVIET_UNION(
            new String[]{"su"}
    ),
    COSTA_RICA(
            new String[]{"cr"}
    ),
    SOUTH_KOREA(
            new String[]{"kr"}
    ),
    CANADA(
            new String[]{"ca"}
    ),
    JAMAICA(
            new String[]{"jm","ja"}
    ),
    SAINT_LUCIA(
            new String[]{"lc","wl"}
    ),
    FINLAND(
            new String[]{"fi","sf"}
    ),
    TRISTAN_DA_CUNHA(
            new String[]{"ta"}
    ),
    SINGAPORE(
            new String[]{"sg"}
    ),
    EGYPT(
            new String[]{"eg"}
    ),
    LEBANON(
            new String[]{"lb","rl"}
    ),
    MONGOLIA(
            new String[]{"mn"}
    ),
    CABO_VERDE(
            new String[]{"cv"}
    ),
    BRITISH_VIRGIN_ISLANDS(
            new String[]{"vg"}
    ),
    HEARD_ISLAND_AND_MCDONALD_ISLANDS(
            new String[]{"hm"}
    ),
    MALI(
            new String[]{"ml"}
    ),
    SAINT_HELENA__ASCENSION_AND_TRISTAN_DA_CUNHA(
            new String[]{"sh"}
    ),
    UNITED_KINGDOM(
            new String[]{"gb","uk"}
    ),
    ZAIRE(
            new String[]{"zr"}
    ),
    MAURITANIA(
            new String[]{"mr"}
    ),
    PAKISTAN(
            new String[]{"pk"}
    ),
    SAUDIÂIRAQI_NEUTRAL_ZONE(
            new String[]{"nt"}
    ),
    SEYCHELLES(
            new String[]{"sc"}
    ),
    ANGOLA(
            new String[]{"ao"}
    ),
    ZAMBIA(
            new String[]{"zm"}
    ),
    GUYANA(
            new String[]{"gy"}
    ),
    SAINT_VINCENT_AND_THE_GRENADINES(
            new String[]{"vc","wv"}
    ),
    BOSNIA_AND_HERZEGOVINA(
            new String[]{"ba"}
    ),
    ESCAPE_CODE(
            new String[]{"oo"}
    ),
    MALAYSIA(
            new String[]{"my"}
    ),
    SAN_MARINO(
            new String[]{"sm"}
    ),
    CZECHIA(
            new String[]{"cz"}
    ),
    GRENADA(
            new String[]{"gd","wg"}
    ),
    NIGERIA(
            new String[]{"ng"}
    ),
    MOROCCO(
            new String[]{"ma"}
    ),
    SERBIA(
            new String[]{"rs"}
    ),
    CAYMAN_ISLANDS(
            new String[]{"ky"}
    ),
    CARIBBEAN_NETHERLANDS(
            new String[]{"bq"}
    ),
    BURMA(
            new String[]{"bu"}
    ),
    VATICAN_CITY(
            new String[]{"va"}
    ),
    NORFOLK_ISLAND(
            new String[]{"nf"}
    ),
    BANGLADESH(
            new String[]{"bd"}
    ),
    ESTONIA(
            new String[]{"ee","ew"}
    ),
    SLOVENIA(
            new String[]{"si"}
    ),
    GEORGIA__COUNTRY(
            new String[]{"ge"}
    ),
    ICELAND(
            new String[]{"is"}
    ),
    GCC_PATENT_OFFICE(
            new String[]{"gc"}
    ),
    FAROE_ISLANDS(
            new String[]{"fo"}
    ),
    LESOTHO(
            new String[]{"ls"}
    ),
    ÃLAND_ISLANDS(
            new String[]{"ax"}
    ),
    MONACO(
            new String[]{"mc"}
    ),
    SRI_LANKA(
            new String[]{"lk"}
    ),
    FEDERATED_STATES_OF_MICRONESIA(
            new String[]{"fm"}
    ),
    JERSEY(
            new String[]{"je"}
    ),
    UKRAINE(
            new String[]{"ua"}
    ),
    BRUNEI(
            new String[]{"bn"}
    ),
    ISRAEL(
            new String[]{"il"}
    ),
    SIERRA_LEONE(
            new String[]{"sl"}
    ),
    CHAD(
            new String[]{"td"}
    ),
    EUROZONE(
            new String[]{"ez"}
    ),
    SUDAN(
            new String[]{"sd"}
    ),
    TUVALU(
            new String[]{"tv"}
    ),
    NEW_ZEALAND(
            new String[]{"nz"}
    ),
    LITHUANIA(
            new String[]{"lt"}
    ),
    SLOVAKIA(
            new String[]{"sk"}
    ),
    TRINIDAD_AND_TOBAGO(
            new String[]{"tt"}
    ),
    NETHERLANDS(
            new String[]{"nl"}
    ),
    YUGOSLAVIA(
            new String[]{"yu"}
    ),
    BENELUX_OFFICE_FOR_INTELLECTUAL_PROPERTY(
            new String[]{"bx"}
    ),
    UGANDA(
            new String[]{"ug"}
    ),
    SYRIA(
            new String[]{"sy"}
    ),
    THAILAND(
            new String[]{"th"}
    ),
    BOLIVIA(
            new String[]{"rb","bo"}
    ),
    BOTSWANA(
            new String[]{"bw"}
    ),
    NETHERLANDS_ANTILLES(
            new String[]{"an"}
    ),
    BRAZIL(
            new String[]{"br"}
    ),
    ORGANISATION_AFRICAINE_DE_LA_PROPRIÃ_TÃ__INTELLECTUELLE(
            new String[]{"oa"}
    ),
    MADAGASCAR(
            new String[]{"rm","mg"}
    ),
    ALBANIA(
            new String[]{"al"}
    ),
    DOMINICAN_REPUBLIC(
            new String[]{"do"}
    ),
    IVORY_COAST(
            new String[]{"ci"}
    ),
    THE_GAMBIA(
            new String[]{"gm"}
    ),
    BRITISH_INDIAN_OCEAN_TERRITORY(
            new String[]{"io"}
    ),
    STATE_OF_PALESTINE(
            new String[]{"ps"}
    ),
    GERMANY(
            new String[]{"de"}
    ),
    SOLOMON_ISLANDS(
            new String[]{"sb"}
    ),
    GUERNSEY(
            new String[]{"gg"}
    ),
    SOUTH_SUDAN(
            new String[]{"ss"}
    ),
    NIGER(
            new String[]{"rn","ne"}
    ),
    COLLECTIVITY_OF_SAINT_MARTIN(
            new String[]{"mf"}
    ),
    MALAWI(
            new String[]{"mw"}
    ),
    GHANA(
            new String[]{"gh"}
    ),
    INDONESIA(
            new String[]{"ri","id"}
    ),
    ITALY(
            new String[]{"it"}
    ),
    REPUBLIC_OF_THE_CONGO(
            new String[]{"cg"}
    ),
    SOUTH_AFRICA(
            new String[]{"za"}
    ),
    PHILIPPINES(
            new String[]{"rp","ph","pi"}
    ),
    KENYA(
            new String[]{"ke"}
    ),
    KYRGYZSTAN(
            new String[]{"kg"}
    ),
    IRAQ(
            new String[]{"iq"}
    ),
    BERMUDA(
            new String[]{"bm"}
    ),
    OFFICE_FOR_HARMONIZATION_IN_THE_INTERNAL_MARKET(
            new String[]{"em"}
    ),
    REPUBLIC_OF_CHINA(
            new String[]{"rc"}
    ),
    UNITED_NATIONS(
            new String[]{"un"}
    ),
    ARGENTINA(
            new String[]{"ra","ar"}
    ),
    CYPRUS(
            new String[]{"cy"}
    ),
    TURKMENISTAN(
            new String[]{"tm"}
    ),
    BAHRAIN(
            new String[]{"bh"}
    ),
    MAURITIUS(
            new String[]{"mu"}
    ),
    MALDIVES(
            new String[]{"mv"}
    ),
    MYANMAR(
            new String[]{"mm"}
    ),
    NICARAGUA(
            new String[]{"ni"}
    ),
    MONTSERRAT(
            new String[]{"ms"}
    ),
    SÃ£O_TOMÃ__AND_PRÃ­NCIPE(
            new String[]{"st"}
    ),
    SURINAME(
            new String[]{"sr"}
    ),
    LATVIA(
            new String[]{"lv"}
    ),
    FEZZAN(
            new String[]{"lf"}
    ),
    PAPUA_NEW_GUINEA(
            new String[]{"pg"}
    ),
    LAOS(
            new String[]{"la"}
    ),
    BELGIUM(
            new String[]{"be"}
    ),
    EUROPEAN_UNION(
            new String[]{"eu"}
    ),
    WORLD_INTELLECTUAL_PROPERTY_ORGANIZATION(
            new String[]{"wo","ib"}
    ),
    RUSSIA(
            new String[]{"ru"}
    ),
    NEW_CALEDONIA(
            new String[]{"nc"}
    ),
    LIBERIA(
            new String[]{"lr"}
    ),
    COLOMBIA(
            new String[]{"co"}
    ),
    AFGHANISTAN(
            new String[]{"af"}
    ),
    KAZAKHSTAN(
            new String[]{"kz"}
    ),
    TURKS_AND_CAICOS_ISLANDS(
            new String[]{"tc"}
    ),
    GUAM(
            new String[]{"gu"}
    ),
    MARTINIQUE(
            new String[]{"mq"}
    ),
    ANTIGUA_AND_BARBUDA(
            new String[]{"ag"}
    ),
    CENTRAL_AFRICAN_REPUBLIC(
            new String[]{"cf"}
    ),
    MOLDOVA(
            new String[]{"md"}
    ),
    THE_BAHAMAS(
            new String[]{"bs"}
    ),
    CEUTA(
            new String[]{"ea"}
    ),
    LIECHTENSTEIN(
            new String[]{"li","fl"}
    ),
    GUINEA_BISSAU(
            new String[]{"gw"}
    ),
    TAJIKISTAN(
            new String[]{"tj"}
    ),
    GREECE(
            new String[]{"gr"}
    ),
    IRAN(
            new String[]{"ir"}
    ),
    TAIWAN(
            new String[]{"tw"}
    ),
    BELIZE(
            new String[]{"bz"}
    ),
    FIJI(
            new String[]{"fj"}
    ),
    NAURU(
            new String[]{"nr"}
    ),
    ROMANIA(
            new String[]{"ro"}
    ),
    GREENLAND(
            new String[]{"gl"}
    ),
    MACAU(
            new String[]{"mo"}
    ),
    DEMOCRATIC_REPUBLIC_OF_THE_CONGO(
            new String[]{"cd"}
    ),
    TUNISIA(
            new String[]{"tn"}
    ),
    NORTHERN_MARIANA_ISLANDS(
            new String[]{"mp"}
    ),
    POLAND(
            new String[]{"pl"}
    ),
    INDIA(
            new String[]{"in"}
    ),
    LUXEMBOURG(
            new String[]{"lu"}
    ),
    SINT_MAARTEN(
            new String[]{"sx"}
    ),
    CURAÃ_AO(
            new String[]{"cw"}
    ),
    HONDURAS(
            new String[]{"hn"}
    ),
    SOMALIA(
            new String[]{"so"}
    ),
    PITCAIRN_ISLANDS(
            new String[]{"pn"}
    ),
    HUNGARY(
            new String[]{"hu"}
    ),
    CLIPPERTON_ISLAND(
            new String[]{"cp"}
    ),
    CZECHOSLOVAKIA(
            new String[]{"cs"}
    ),
    SPAIN(
            new String[]{"es"}
    ),
    UNITED_ARAB_EMIRATES(
            new String[]{"ae"}
    ),
    AFRICAN_REGIONAL_INTELLECTUAL_PROPERTY_ORGANIZATION(
            new String[]{"ap"}
    ),
    ISLE_OF_MAN(
            new String[]{"im"}
    ),
    CUBA(
            new String[]{"cu"}
    ),
    METROPOLITAN_FRANCE(
            new String[]{"fx"}
    ),
    MALTA(
            new String[]{"mt"}
    ),
    ANDORRA(
            new String[]{"ad"}
    ),
    SOUTH_GEORGIA_AND_THE_SOUTH_SANDWICH_ISLANDS(
            new String[]{"gs"}
    ),
    DJIBOUTI(
            new String[]{"dj"}
    ),
    BELARUS(
            new String[]{"by"}
    ),
    JORDAN(
            new String[]{"jo"}
    ),
    SAUDI_ARABIA(
            new String[]{"sa"}
    ),
    JAPAN(
            new String[]{"jp"}
    ),
    PARAGUAY(
            new String[]{"py"}
    ),
    FALKLAND_ISLANDS(
            new String[]{"fk"}
    ),
    DOMINICA(
            new String[]{"dm"}
    ),
    TURKEY(
            new String[]{"tr"}
    ),
    SAMOA(
            new String[]{"ws"}
    ),
    EUROPEAN_PATENT_ORGANISATION(
            new String[]{"ep"}
    ),
    BHUTAN(
            new String[]{"bt"}
    ),
    URUGUAY(
            new String[]{"uy"}
    ),
    DIEGO_GARCIA(
            new String[]{"dg"}
    ),
    GUINEA(
            new String[]{"gn"}
    ),
    PANAMA(
            new String[]{"pa"}
    ),
    MARSHALL_ISLANDS(
            new String[]{"mh"}
    ),
    PUERTO_RICO(
            new String[]{"pr"}
    ),
    WALLIS_AND_FUTUNA(
            new String[]{"wf"}
    ),
    GUATEMALA(
            new String[]{"gt"}
    ),
    CHILE(
            new String[]{"cl"}
    ),
    BURUNDI(
            new String[]{"bi"}
    ),
    CHINA(
            new String[]{"cn"}
    ),
    HAITI(
            new String[]{"rh","ht"}
    ),
    GUADELOUPE(
            new String[]{"gp"}
    ),
    CAMEROON(
            new String[]{"cm"}
    ),
    ASCENSION_ISLAND(
            new String[]{"ac"}
    ),
    BULGARIA(
            new String[]{"bg"}
    );

    public static final int ISO_3166_1_ALPHA_2 = 0;
    public static final int MAX_SIZE = 1;

    public class DefinedLocal {
        protected Locale locale;
        protected String shortName;

        public DefinedLocal(Locale locale, String shortName) {
            this.locale = locale;
            this.shortName = shortName;
        }

        public String getShortName() {
            return shortName;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            DefinedLocal that = (DefinedLocal) o;
            return locale == that.locale &&
                    Objects.equals(shortName, that.shortName);
        }

        @Override
        public int hashCode() {
            return Objects.hash(locale, shortName);
        }
    }

    private DefinedLocal defaultDefined;
    private String[] shortNames;

    Locale(String[] shortName) {
        this.shortNames = shortNames;
    }

    public String[] getFromTyp(int typ) {
        switch (typ) {
            case ISO_3166_1_ALPHA_2:
                return shortNames;
        }
        return new String[0];
    }

    public DefinedLocal defaultDefined() {
        if (defaultDefined == null) {
            defaultDefined = new DefinedLocal(this, getShortNames()[0]);
        }
        return defaultDefined;
    }
    public DefinedLocal from(String shortName) {
        if (!asList(shortNames).contains(shortName)) {
            return null;
        }
        return new DefinedLocal(this, shortName);
    }
    public String[] getShortNames() {
        return getFromTyp(ISO_3166_1_ALPHA_2);
    }

    public static Locale[] getLocalesFromTypAndName(int typ, String name, Locale...possibleValues) {
        List<Locale> locales = new ArrayList<>();

        if (possibleValues == null) {
            possibleValues = Locale.values();
        }

        for (Locale locale:possibleValues) {
            if (asList(locale.getFromTyp(typ)).contains(name)) {
                locales.add(locale);
            }
        }
        return locales.toArray(new Locale[0]);
    }

    public static Collection<DefinedLocal> from(Object...nameOrLocale) {
        List<DefinedLocal> locals = new ArrayList<>();
        if (nameOrLocale == null || nameOrLocale.length == 0) {
            return locals;
        }

        for (Object nameOrLocal:nameOrLocale) {
            if (nameOrLocal instanceof Locale) {
                locals.add(((Locale) nameOrLocal).defaultDefined());
                continue;
            } else if (!(nameOrLocal instanceof String[])) {
                continue;
            }

            Locale[] possibleLocals = null;

            String[] names = (String[]) nameOrLocal;
            String[] namesTheme = new String[names.length];
            for (int i = 0; i < names.length; i++) {
                String name = names[i];
                if (name == null) {
                    continue;
                }
                namesTheme[i] = name;
                possibleLocals = getLocalesFromTypAndName(i, name, possibleLocals);
            }

            if (possibleLocals == null) {
                continue;
            }

            List<String[]> allNames = new ArrayList<String[]>();
            allNames.add(namesTheme);
            for (int i = 0; i < names.length; i++) {
                String name = names[i];
                if (name == null) {
                    List<String> possibleNames = new ArrayList<String>();
                    for (Locale possibleLocal:possibleLocals) {
                        possibleNames.addAll(asList(possibleLocal.getFromTyp(i)));
                    }

                    List<String[]> newAllNames = new ArrayList<>();
                    for (String[] neededNames:allNames) {
                        String[] themedNames = neededNames;
                        for (String possibleName:possibleNames) {
                            themedNames[i] = possibleName;
                            newAllNames.add(themedNames);
                            themedNames = neededNames;
                        }
                    }
                    allNames = newAllNames;
                }
            }

            List<Class> parameterTypeList = new ArrayList<Class>();
            for (int i = 0; i < MAX_SIZE; i++) {
                parameterTypeList.add(String.class);
            }

            Class[] parameterTypes = parameterTypeList.toArray(new Class[0]);
            for(Locale possibleLocal:possibleLocals) {
                Method fromMethod = null;
                try {
                    fromMethod = Locale.class.getMethod("from", parameterTypes);
                    for (String[] params:allNames) {
                        DefinedLocal definedLocal = (DefinedLocal) fromMethod.invoke(possibleLocal, params);
                        if (definedLocal == null) {
                            continue;
                        }
                        locals.add(definedLocal);
                    }
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    // TODO better error handling
                }
            }
        }
        return locals;
    }
}