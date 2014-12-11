#define NON_LIB_STRING
#define NON_LIB_INPUT
#define NON_LIB_RAND
#define NON_LIB_PRINTF
#include <nonstdlib.h>

int main()
{
	int hp = 100, dragonHp = 1000;
	printf("You are sent out of the kingdom ");
	printf("by the King to slay a dragon.\n");
	while (dragonHp > 0 && hp > 0)
	{
		int dmg, ddmg;
		char *str = 3000;
		char *cstr = 3081;
		printf("The dragon is unpredictable. What are you going to do?\n");
		printf("Your hp: %d, Dragon hp: %d\n", hp, dragonHp);
		printf("(Attack: 1 | Heal: 2): ");
		dmg = genRand(hp);
		ddmg = genRand(100);
		gets(str,1);
		cstr = to_c_str(str,cstr)
		if (cstr[0] - '0' == 1)
		{
			printf("You deal %d damage to the dragon!\n",dmg);
			printf("The dragon also attacks you for %d damage\n",ddmg);
			hp -= ddmg;
			dragonHp -= dmg;
		} else {
			printf("The dragon attacks you while you heal!\n");
			printf("You heal for %d\n",dmg);
			printf("You take %d damage\n",ddmg);
			hp += dmg;
			hp -= ddmg;
		}
	}
	if (dragonHp < 1)
	{
		printf("You have slain the dragon!\n");
	} else {
		printf("Whelp, that didn't end well did it?\n");
	}
	return 0;
}
