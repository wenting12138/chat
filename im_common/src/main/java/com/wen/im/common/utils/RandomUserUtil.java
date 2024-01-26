package com.wen.im.common.utils;

import cn.hutool.core.util.IdUtil;

import java.util.Random;

/**
 * 生成随机用户信息
 *
 * @author ppp
 * @date 2023-05-12
 */
public class RandomUserUtil {
    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            UserInfo randomUser = getRandomUser();
            System.out.println(randomUser.toString());
        }
        System.out.println("耗时：单位毫秒" + (System.currentTimeMillis()-start));
    }

    public static UserInfo getRandomUser() {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(IdUtil.getSnowflakeNextId());
        int sexInt = getRandomSexInt();
        userInfo.setSex(sexInt);
        userInfo.setName(sexInt == 1 ? getRandomBoyName() : getRandomGirlName());
        userInfo.setAge(getRandomAge());
        userInfo.setHeight(getRandomHeight());
        userInfo.setWeight(getRandomWeight());
        userInfo.setPhone(getRandomPhone());
        userInfo.setEmail(getRandomQQEmail());
        return userInfo;
    }


    /**
     * 复姓出现的几率(0--100)
     */
    private static final int SURNAME_PROBABILITY = 5;
    private static final Random RANDOM = new Random();

    /**
     * 生成随机数(最大值限制)
     */
    public static int randomInt(int maxNum) {
        return RANDOM.nextInt(maxNum);
    }


    /**
     * 获取随机男生姓名
     */
    public static String getRandomBoyName() {
        String boyName = "伟刚勇毅俊峰强军平保东文辉力明永健世广志义兴良海山仁波宁贵福生龙元全国胜学祥才发武新利清飞彬富顺信子杰涛昌成康星光天达" +
                "安岩中茂进林有坚和彪博诚先敬震振壮会思群豪心邦承乐绍功松善厚庆磊民友裕河哲江超浩亮政谦亨奇固之轮翰朗伯宏言若鸣朋斌梁栋维启克伦翔旭鹏泽" +
                "晨辰士以建家致树炎德行时泰盛雄琛钧冠策腾楠榕风航弘";
        int bodNameIndexOne = randomInt(boyName.length());
        int bodNameIndexTwo = randomInt(boyName.length());
        if (randomInt(100) > SURNAME_PROBABILITY) {
            int familyOneNameIndex = randomInt(FAMILY_ONE_NAME.length());
            return FAMILY_ONE_NAME.substring(familyOneNameIndex, familyOneNameIndex + 1) +
                    boyName.substring(bodNameIndexOne, bodNameIndexOne + 1) +
                    boyName.substring(bodNameIndexTwo, bodNameIndexTwo + 1);
        } else {
            int familyTwoNameIndex = randomInt(FAMILY_TWO_NAME.length());
            familyTwoNameIndex = familyTwoNameIndex % 2 == 0 ? familyTwoNameIndex : familyTwoNameIndex - 1;
            return FAMILY_TWO_NAME.substring(familyTwoNameIndex, familyTwoNameIndex + 2) +
                    boyName.substring(bodNameIndexOne, bodNameIndexOne + 1) +
                    boyName.substring(bodNameIndexTwo, bodNameIndexTwo + 1);
        }
    }


    /**
     * 获取女生姓名
     */
    public static String getRandomGirlName() {
        String girlName = "秀娟英华慧巧美娜静淑惠珠翠雅芝玉萍红娥玲芬芳燕彩春菊兰凤洁梅琳素云莲真环雪荣爱妹霞香月莺媛艳瑞凡佳嘉琼勤珍贞莉桂娣叶璧" +
                "璐娅琦晶妍茜秋珊莎锦黛青倩婷姣婉娴瑾颖露瑶怡婵雁蓓纨仪荷丹蓉眉君琴蕊薇菁梦岚苑婕馨瑗琰韵融园艺咏卿聪澜纯毓悦昭冰爽琬茗羽希宁欣飘育滢馥" +
                "筠柔竹霭凝晓欢霄枫芸菲寒伊亚宜可姬舒影荔枝思丽";
        int bodNameIndexOne = randomInt(girlName.length());
        int bodNameIndexTwo = randomInt(girlName.length());
        if (randomInt(100) > SURNAME_PROBABILITY) {
            int familyOneNameIndex = randomInt(FAMILY_ONE_NAME.length());
            return FAMILY_ONE_NAME.substring(familyOneNameIndex, familyOneNameIndex + 1) +
                    girlName.substring(bodNameIndexOne, bodNameIndexOne + 1) +
                    girlName.substring(bodNameIndexTwo, bodNameIndexTwo + 1);
        } else {
            int familyTwoNameIndex = randomInt(FAMILY_TWO_NAME.length());
            familyTwoNameIndex = familyTwoNameIndex % 2 == 0 ? familyTwoNameIndex : familyTwoNameIndex - 1;
            return FAMILY_TWO_NAME.substring(familyTwoNameIndex, familyTwoNameIndex + 2) +
                    girlName.substring(bodNameIndexOne, bodNameIndexOne + 1) +
                    girlName.substring(bodNameIndexTwo, bodNameIndexTwo + 1);
        }
    }


    /**
     * 获取随机手机号
     */
    public static String getRandomPhone() {
        int phoneTwoRandomIndex = randomInt(4);
        String phoneTwoNum = "6379";
        return "1" + phoneTwoNum.substring(phoneTwoRandomIndex, phoneTwoRandomIndex + 1) + (100000000 + randomInt(899999999));
    }


    /**
     * 获取随机qq邮箱
     */
    public static String getRandomQQEmail() {
        return ("" + RANDOM.nextLong()).substring(10) + "@qq.com";
    }

    /**
     * 获取随机性别
     */
    public static int getRandomSexInt() {
        return randomInt(2);
    }


    /**
     * 获取随机性别
     */
    public static String getRandomSexStr() {
        return randomInt(2) % 2 == 0 ? "女" : "男";
    }

    /**
     * 获取随机范围数字
     */
    public static int getRandomNum(int min, int max) {
        return min + RANDOM.nextInt(max - min);
    }


    /**
     * 获取随机年龄
     */
    public static int getRandomAge(int min, int max) {
        return getRandomNum(18, 25);
    }

    /**
     * 获取随机年龄(18-35)
     */
    public static int getRandomAge() {
        return getRandomNum(18, 35);
    }

    /**
     * 获取随机身高(140-190)
     */
    public static int getRandomHeight() {
        return getRandomNum(140, 190);
    }

    /**
     * 获取随机身高
     */
    public static int getRandomHeight(int min, int max) {
        return getRandomNum(min, max);
    }


    /**
     * 获取随机身高(50-70)
     */
    public static int getRandomWeight() {
        return getRandomNum(50, 70);
    }

    /**
     * 获取随机体重
     */
    public static int getRandomWeight(int min, int max) {
        return getRandomNum(min, max);
    }

    private static final String FAMILY_ONE_NAME = "赵钱孙李周吴郑王冯陈褚卫蒋沈韩杨朱秦尤许何吕施张孔曹严华金魏陶姜戚谢邹喻水云苏潘葛奚范彭郎鲁韦昌马苗凤花方俞任" +
            "袁柳鲍史唐费岑薛雷贺倪汤滕殷罗毕郝邬安常乐于时傅卞齐康伍余元卜顾孟平黄和穆萧尹姚邵湛汪祁毛禹狄米贝明臧计成戴宋茅庞熊纪舒屈项祝董粱杜阮" +
            "席季麻强贾路娄危江童颜郭梅盛林刁钟徐邱骆高夏蔡田胡凌霍万柯卢莫房缪干解应宗丁宣邓郁单杭洪包诸左石崔吉龚程邢滑裴陆荣翁荀羊甄家封芮储靳邴" +
            "松井富乌焦巴弓牧隗山谷车侯伊宁仇祖武符刘景詹束龙叶幸司韶黎乔苍双闻莘劳逄姬冉宰桂牛寿通边燕冀尚农温庄晏瞿茹习鱼容向古戈终居衡步都耿满弘" +
            "国文东殴沃曾关红游盖益桓公晋楚闫";

    private static final String FAMILY_TWO_NAME = "欧阳太史端木上官司马东方独孤南宫万俟闻人夏侯诸葛尉迟公羊赫连澹台皇甫宗政濮阳公冶太叔申屠公孙慕容仲孙钟离长孙宇" +
            "文司徒鲜于司空闾丘子车亓官司寇巫马公西颛孙壤驷公良漆雕乐正宰父谷梁拓跋夹谷轩辕令狐段干百里呼延东郭南门羊舌微生公户公玉公仪梁丘公仲公上" +
            "公门公山公坚左丘公伯西门公祖第五公乘贯丘公皙南荣东里东宫仲长子书子桑即墨达奚褚师吴铭";


    public static class UserInfo {
        /**
         * 雪花id
         */
        private Long id;
        /**
         * 姓名
         */
        private String name;
        /**
         * 年龄
         */
        private Integer age;
        /**
         * 性别 0：女；1：男
         */
        private Integer sex;
        /**
         * 性别
         */
        private String sexStr;
        /**
         * 身高 单位：cm
         */
        private Integer height;
        /**
         * 体重 单位：kg
         */
        private Integer weight;

        /**
         * 手机号
         */
        private String phone;
        /**
         * 电子邮件
         */
        private String email;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }

        public Integer getSex() {
            return sex;
        }

        public void setSex(Integer sex) {
            this.sex = sex;
            this.sexStr = sex == 1 ? "男" : "女";
        }

        public String getSexStr() {
            return sexStr;
        }

        public Integer getHeight() {
            return height;
        }

        public void setHeight(Integer height) {
            this.height = height;
        }

        public Integer getWeight() {
            return weight;
        }

        public void setWeight(Integer weight) {
            this.weight = weight;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        @Override
        public String toString() {
            return "UserInfo{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", age=" + age +
                    ", sex=" + sex +
                    ", sexStr='" + sexStr + '\'' +
                    ", height=" + height +
                    ", weight=" + weight +
                    ", phone='" + phone + '\'' +
                    ", email='" + email + '\'' +
                    '}';
        }
    }


}