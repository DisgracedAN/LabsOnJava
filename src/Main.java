import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Random;
import java.util.Locale;

enum Specialization {
    Warrior("Воин"),
    Hunter("Охотник"),
    Sorcerer("Маг");

    private final String name;

    Specialization(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}


enum ItemType{
    Key("Ключ"),
    Weapon("Оружие"),
    Armor("Броня"),
    Consumables("Расходуемый Предмет");

    private final String name;
    ItemType(String name){
        this.name=name;
    }
    public String getName(){return name;}
}


//Тип встречающихся NPC
enum EntityType {
    Monster("Монстр"),
    Human("Человек"),
    Animal("Животное");
    private final String name;

    EntityType(String name){
        this.name=name;
    }

    public String getName() {return name;}
}



//Класс персонажа
class CharacterClass {
    String name;
    Specialization specialization;//Класс
    double HealthBar;//Здоровье
    double Stamina;//Выносливость
    double ManaPoints; //Очки маны
    int SpellCells;//Количество ячеек заклинаний
    double Damage;//Урон

    public CharacterClass() {
        this.name = "";
        this.specialization = Specialization.Warrior;
        this.HealthBar = 0;
        this.Stamina = 0;
        this.ManaPoints = 0;
        this.SpellCells = 0;
        this.Damage = 0;
    }

    public double getHealthBar() {
        return HealthBar;
    }

    public double getDamage() {
        return Damage;
    }

    public String getName() {
        return name;
    }

    public void setCharacterClass(String pName, Specialization spec, double health, double stamina, double mana, int spellCells, double damage) {
        this.name = pName;
        this.specialization = spec;
        this.HealthBar = health;
        this.Stamina = stamina;
        this.ManaPoints = mana;
        this.SpellCells = spellCells;
        this.Damage = damage;
    }


    public void ClassCreation() {
        int PlayerType;
        System.out.println("Введите имя:");
        String Name;
        Scanner in = new Scanner(System.in, "cp1251");
        Name = in.nextLine();
        this.name=Name;

        do {
            System.out.println("Выберете класс персонажа: 1)Воин 2)Охотник 3)Колдун");
            while (!in.hasNextInt()) {
                System.out.println("Введите число от 1 до 3:");
                in.next();
            }
            PlayerType = in.nextInt();
        } while (PlayerType < 1 || PlayerType > 3);
        PlayerType--;
        Specialization spec = Specialization.values()[PlayerType];

        this.specialization=spec;
        switch (spec) {
            case Warrior:
                this.Damage = 20;
                this.HealthBar= 100;
                this.ManaPoints = 5;
                this.Stamina = 50;
                this.SpellCells = 0;
                break;
            case Hunter:
                this.Damage = 15;
                this.HealthBar = 75;
                this.ManaPoints = 20;
                this.Stamina = 75;
                this.SpellCells = 2;
                break;
            case Sorcerer:
                this.Damage = 10;
                this.HealthBar = 50;
                this.ManaPoints = 60;
                this.Stamina = 50;
                this.SpellCells = 5;
                break;
            default:
                throw new IllegalArgumentException("Неизвестная специализация: ");

        }

        System.out.println("Персонаж успешно создан!");
    }


}

class PlayableCharacter {
    CharacterClass Type;
    Inventory inventory;

    public PlayableCharacter(){
        this.Type = new CharacterClass();
        this.inventory = new Inventory();
    };

    public void addToInventory(Item item){
        inventory.AddItem(item);
    }

    public CharacterClass getType(){return Type;}

    public Inventory getInventory() {
        return inventory;
    }

    public void PrintStats(){
        System.out.println("Ваше имя: " + Type.name);
        System.out.println("Ваш Класс: " + Type.specialization.getName());
        System.out.println("Ваша статистика:");System.out.println("Здоровье: " + Type.HealthBar);
        System.out.println("Урон: " + Type.Damage);System.out.println("Выносливость: " + Type.Stamina);System.out.println("Ячейки заклинаний: " + Type.SpellCells);
        System.out.println("ОМ: " + Type.ManaPoints);

    }
    void ShowInventory(){
        System.out.println("Содержимое инвентаря:");
        for (int i = 0; i < inventory.ActiveSlots; i++) {
            Item item = inventory.items[i];
            System.out.println("Предмет: " + (i + 1));
            System.out.println(" Тип: " + item.getItemType().getName());
            System.out.println(" Название: " + item.getName());
            System.out.println(" Описание: " + item.getDescription());
        }
    }

    public void getDamaged(double damage){
        if(Type.HealthBar>0){
            Type.HealthBar-=damage;
            if(Type.HealthBar<0){
                Type.HealthBar=0;
            }
            System.out.printf("%s получил %.2f урона. Текущее здоровье: %.2f\n", Type.name, damage,Type.HealthBar);
        }
    }

    public void setStartItem(){
        Item key = new Item();
        key.setItem(ItemType.Key, "Башенный ключ", "Старый, потёртый ключ, открывающий дверь в Башню", 0);
        this.addToInventory(key);
        Item healthPotion = new Item();
        healthPotion.setItem(ItemType.Consumables, "Зелье жизни", "Маленький пузырёк красной жидкости. Единоразово восстанавливает здоровье", 70);
        this.addToInventory(healthPotion); // распределение вещей в зависимости от "специализации"
        if (this.Type.specialization == Specialization.Warrior) {
            Item palladinSword = new Item();
            palladinSword.setItem(ItemType.Weapon, "Меч Палладина", "Меч, отливающий серебряным блеском. Отлично справляется с нечистью", 40);
            Item palladinArmor = new Item();
            palladinArmor.setItem(ItemType.Armor, "Кираса Палладина", "Серебристая кираса с гербом Королевства", 45);
            this.addToInventory(palladinSword);
            this.addToInventory(palladinArmor);
        }
        if (this.Type.specialization == Specialization.Hunter) {
            Item assassinDagger = new Item();
            assassinDagger.setItem(ItemType.Weapon, "Кинжал Ассассина", "Клинок из чёрной стали, предназначеный для вероломных ударов в спину", 40);
            Item huntersBow = new Item();
            huntersBow.setItem(ItemType.Weapon, "Лук Охотника", "Простой деревянный лук, ценный среди охотников за простоту и эффективность", 50);
            Item huntersKilt = new Item();
            huntersKilt.setItem(ItemType.Armor, "Одеяние Охотника", "Лёгкая накидка королевских охотников. Простая и эргономичная, не сковывающая движения", 55);
            this.addToInventory(assassinDagger);
            this.addToInventory(huntersBow);
            this.addToInventory(huntersKilt);
        }
        if (this.Type.specialization == Specialization.Sorcerer) {
            Item basedStuff = new Item();
            Item sorcerersRobe = new Item();
            basedStuff.setItem(ItemType.Weapon, "Посох Колдуна", "Обычный посох из дерева и камня души. Позволяет воплощать мысль в магию", 33);
            sorcerersRobe.setItem(ItemType.Armor, "Роба Мага-Новичка", "Роба начинающего мага. Ходят легенты, что хранит в себе частичку магической силы", 55);
            this.addToInventory(basedStuff);
            this.addToInventory(sorcerersRobe);
        }
    }


}

class Inventory {
    Item[] items=new Item[100];//Массив предметов
    int ActiveSlots;//Свободные слоты

    public Inventory(){
        this.items=new Item[100];
        this.ActiveSlots=0;
    };
    public void AddItem(Item item){
        if (ActiveSlots < 100) {
            items[ActiveSlots] = item;
            ActiveSlots++;
        }
        else { System.out.println("Инвентарь полон!");}
    }
}


//Предмет
class Item {
    ItemType itemType;//Тип предмета
    String Name;//Название
    String Description;//Краткое описание
    int DropChance;//Шанс выпадения с NPC

    public Item(){
        this.itemType=ItemType.Key;
        this.Name="";
        this.Description="";
        this.DropChance=0;

    }


    public void setItem(ItemType itemtype, String name, String desc, int dropchance){
        this.itemType=itemtype;
        this.Name=name;
        this.Description=desc;
        this.DropChance=dropchance;
    };
    public ItemType getItemType() { return itemType; }
    public String getName() { return Name; }
    public String getDescription() { return Description;}
    public int getDropChance(){return DropChance;}
}

//NPC
class Entity {
    String Name;
    EntityType Type;//Тип NPC
    boolean Friendly;//Является ли он дружественным(1-да 0-нет)
    double HealthBar;//Здоровье
    double Stamina;//Выносливость
    double ManaPoints;//Очки маны
    double Damage;//Урон

    public Entity(){
        this.Name="";
        this.ManaPoints=0;
        this.Damage=0;
        this.Stamina=0;
        this.Type=EntityType.Monster;
        this.Friendly=false;
        this.ManaPoints=0;
    }

    public double getHealthBar() {
        return HealthBar;
    }

    public double getDamage() {
        return Damage;
    }

    public boolean isFriendly() {
        return Friendly;
    }

    public void setEntity(String name, EntityType type, boolean friendly, double health, double stamina, double mana, double damage){
        this.Name=name;
        this.Type=type;
        this.Friendly=friendly;
        this.HealthBar=health;
        this.Stamina=stamina;
        this.ManaPoints=mana;
        this.Damage=damage;
    }

    public void printEntity(){
        System.out.println("Имя NPC: " + Name);
        System.out.println("Тип NPC: " + Type.getName());
        System.out.println("Cтатистика:");
        System.out.println("Здоровье: " + HealthBar);
        System.out.println("Урон: " + Damage);
        System.out.println("Выносливость: " + Stamina);
        System.out.println("ОМ: " + ManaPoints);

    }

    public void GetDamaged(PlayableCharacter character){
        if(HealthBar > 0) {
            HealthBar -= character.Type.Damage;
            if (HealthBar < 0) {
                HealthBar = 0;
            }
            System.out.printf("%s нанёс %.2f урона по %s. Здоровье %s составляет %.2f%n", character.Type.name, character.Type.Damage, Name, Name, HealthBar);
        }
    }

    public void EntityDied(PlayableCharacter Character, Item item){
        Random rand = new Random();
        if (HealthBar <= 0) {
            System.out.println(Name + " погиб!"); // Выпадение предмета
            int dropChance = rand.nextInt(100) + 1;
            if (dropChance <= item.getDropChance()) {
                System.out.println(Name + " выронил: " + item.getName());
                System.out.println("Подобрать? (1-да 2-нет)");
                Scanner scanner = new Scanner(System.in);
                int ch;
                do {
                    while (!scanner.hasNextInt()) {
                        System.out.println("Введите число 1 или 2:");
                        scanner.next();
                    }
                    ch = scanner.nextInt();
                } while (ch < 1 || ch > 2);
                if (ch == 1) {
                    Character.addToInventory(item);
                }
            } else
            { System.out.println(Name + " ничего не выронил");
            }
        }

    }

}

class PlayableCharacterManager {

    private List<PlayableCharacter> characters;
    private int capacity;

    public PlayableCharacterManager() {
        this.characters = new ArrayList<>();
        this.capacity = 10; // Начальная емкость
    }


    public void addCharacter(PlayableCharacter character) {
        if (characters.size() >= capacity) {
            resize();
        }
        characters.add(character);
    }


    public void removeCharacter(int index) {
        if (index >= 0 && index < characters.size()) {
            characters.remove(index);
        }
    }


    public PlayableCharacter getCharacter(int index) {
        if (index >= 0 && index < characters.size()) {
            return characters.get(index);
        }
        return null;
    }

    public int getSize() {
        return characters.size();
    }


    public void update(PlayableCharacter hero) {
        for (int i = 0; i < this.getSize(); ++i) {
            if (this.characters.get(i).Type.name.equals(hero.Type.name)) {
                this.characters.set(i, hero);
                return;
            }
        }
    }


    private void resize() {
        capacity *= 2;
        List<PlayableCharacter> newCharacters = new ArrayList<>(capacity);
        newCharacters.addAll(characters);
        characters = newCharacters;
    }

}


//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        // Настройка локали для русского языка
        Locale.setDefault(new Locale("ru", "RU"));
        System.out.print("Программа запущена\n");

        PlayableCharacterManager saves = new PlayableCharacterManager();



        PlayableCharacter hero = new PlayableCharacter();

        hero.getType().ClassCreation();

        saves.addCharacter(hero);

        System.out.println("Просмотр статистики после создания");
        hero.PrintStats();
        hero.getInventory();
        hero.setStartItem();
        System.out.println("Просмотр инвентаря");
        hero.ShowInventory();
        // Создание врага
        Entity gobbo = new Entity();
        gobbo.setEntity("Гоблин-Воин", EntityType.Monster, false, 50, 2, 3, 5);
        System.out.println("Просмотр информации о NPC");
        gobbo.printEntity();
        // Создание предмета для выпадения
        Item money = new Item();
        money.setItem(ItemType.Consumables, "Монетка", "Золотая монетка, блестящая на солнце.", 40);
        // Процесс боя
        do {
            gobbo.GetDamaged(hero);
            if (gobbo.getHealthBar() <= 0)
                break;
            hero.getDamaged(gobbo.getDamage());
            if (hero.getType().getHealthBar() <= 0)
                break;
        } while (gobbo.getHealthBar() != 0 || hero.getType().getHealthBar() != 0);
        gobbo.EntityDied(hero, money);
        System.out.println("Просмотр послебоевой статистики");
        hero.PrintStats();
        System.out.println("Просмотр послебоевого инвентаря");
        hero.ShowInventory();
        saves.update(hero);
        PlayableCharacter hero2 = new PlayableCharacter();
        hero2.getType().ClassCreation();
        hero2.getInventory();
        hero2.setStartItem();
        saves.addCharacter(hero2);
        System.out.println("Количество персонажей: " + saves.getSize());
        for (int i = 0; i < saves.getSize(); ++i) {
            System.out.println(saves.getCharacter(i).getType().getName());
        }
        System.out.println("Введите номер персонажа для вывода статистики:");
        System.out.println("Всего персонажей: " + saves.getSize());
        Scanner scanner = new Scanner(System.in);
        int choice;
        do {
            while (!scanner.hasNextInt()) {
                System.out.println("Введите число:");
                scanner.next();
            }
            choice = scanner.nextInt();
        } while (choice < 0 || choice > saves.getSize());
        choice--;
        PlayableCharacter character = saves.getCharacter(choice);
        character.PrintStats();
        character.ShowInventory();



    }
}