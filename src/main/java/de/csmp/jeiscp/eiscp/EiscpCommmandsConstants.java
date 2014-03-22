package de.csmp.jeiscp.eiscp;


/** GENERATED FILE - do not modify */
public class EiscpCommmandsConstants {

/**
 * CommandBlock
 * ISCP Command prefix: TGA
 * 12V Trigger A Command
 */
public static final String _12V_TRIGGER_A = "12v-trigger-a";
public static final String _12V_TRIGGER_A_ISCP = "TGA";

/**
 * ISCP Command: TGA00
 * sets 12V Trigger A Off
 */
public static final String _12V_TRIGGER_A_OFF = "12v-trigger-a::off";
// public static final String _12V_TRIGGER_A_OFF_ISCP = "TGA00";

/**
 * ISCP Command: TGA01
 * sets 12V Trigger A On
 */
public static final String _12V_TRIGGER_A_ON = "12v-trigger-a::on";
// public static final String _12V_TRIGGER_A_ON_ISCP = "TGA01";

/**
 * CommandBlock
 * ISCP Command prefix: TGB
 * 12V Trigger B Command
 */
public static final String _12V_TRIGGER_B = "12v-trigger-b";
public static final String _12V_TRIGGER_B_ISCP = "TGB";

/**
 * ISCP Command: TGB00
 * sets 12V Trigger B Off
 */
public static final String _12V_TRIGGER_B_OFF = "12v-trigger-b::off";
// public static final String _12V_TRIGGER_B_OFF_ISCP = "TGB00";

/**
 * ISCP Command: TGB01
 * sets 12V Trigger B On
 */
public static final String _12V_TRIGGER_B_ON = "12v-trigger-b::on";
// public static final String _12V_TRIGGER_B_ON_ISCP = "TGB01";

/**
 * CommandBlock
 * ISCP Command prefix: TGC
 * 12V Trigger C Command
 */
public static final String _12V_TRIGGER_C = "12v-trigger-c";
public static final String _12V_TRIGGER_C_ISCP = "TGC";

/**
 * ISCP Command: TGC00
 * sets 12V Trigger C Off
 */
public static final String _12V_TRIGGER_C_OFF = "12v-trigger-c::off";
// public static final String _12V_TRIGGER_C_OFF_ISCP = "TGC00";

/**
 * ISCP Command: TGC01
 * sets 12V Trigger C On
 */
public static final String _12V_TRIGGER_C_ON = "12v-trigger-c::on";
// public static final String _12V_TRIGGER_C_ON_ISCP = "TGC01";

/**
 * CommandBlock
 * ISCP Command prefix: IFA
 * Audio Infomation Command
 */
public static final String AUDIO_INFOMATION = "audio-infomation";
public static final String AUDIO_INFOMATION_ISCP = "IFA";

/**
 * ISCP Command: IFAnnnnn:nnnnn
 * Infomation of Audio(Same Immediate Display ',' is separator of infomations)
 */
public static final String AUDIO_INFOMATION_NULL = "audio-infomation::null";
// public static final String AUDIO_INFOMATION_NULL_ISCP = "IFAnnnnn:nnnnn";

/**
 * ISCP Command: IFAQSTN
 * gets Infomation of Audio
 */
public static final String AUDIO_INFOMATION_QUERY = "audio-infomation::query";
// public static final String AUDIO_INFOMATION_QUERY_ISCP = "IFAQSTN";

/**
 * CommandBlock
 * ISCP Command prefix: AMT
 * Audio Muting Command
 */
public static final String AUDIO_MUTING = "audio-muting";
public static final String AUDIO_MUTING_ISCP = "AMT";

/**
 * ISCP Command: AMT00
 * sets Audio Muting Off
 */
public static final String AUDIO_MUTING_OFF = "audio-muting::off";
public static final String AUDIO_MUTING_OFF_ISCP = "AMT00";

/**
 * ISCP Command: AMT01
 * sets Audio Muting On
 */
public static final String AUDIO_MUTING_ON = "audio-muting::on";
public static final String AUDIO_MUTING_ON_ISCP = "AMT01";

/**
 * ISCP Command: AMTQSTN
 * gets the Audio Muting State
 */
public static final String AUDIO_MUTING_QUERY = "audio-muting::query";
// public static final String AUDIO_MUTING_QUERY_ISCP = "AMTQSTN";

/**
 * ISCP Command: AMTTG
 * sets Audio Muting Wrap-Around
 */
public static final String AUDIO_MUTING_TOGGLE = "audio-muting::toggle";
// public static final String AUDIO_MUTING_TOGGLE_ISCP = "AMTTG";

/**
 * CommandBlock
 * ISCP Command prefix: SLA
 * Audio Selector Command
 */
public static final String AUDIO_SELECTOR = "audio-selector";
public static final String AUDIO_SELECTOR_ISCP = "SLA";

/**
 * ISCP Command: SLA02
 * sets ANALOG
 */
public static final String AUDIO_SELECTOR_ANALOG = "audio-selector::analog";
// public static final String AUDIO_SELECTOR_ANALOG_ISCP = "SLA02";

/**
 * ISCP Command: SLA07
 * sets ARC
 */
public static final String AUDIO_SELECTOR_ARC = "audio-selector::arc";
// public static final String AUDIO_SELECTOR_ARC_ISCP = "SLA07";

/**
 * ISCP Command: SLA00
 * sets AUTO
 */
public static final String AUDIO_SELECTOR_AUTO = "audio-selector::auto";
// public static final String AUDIO_SELECTOR_AUTO_ISCP = "SLA00";

/**
 * ISCP Command: SLA06
 * sets BALANCE
 */
public static final String AUDIO_SELECTOR_BALANCE = "audio-selector::balance";
// public static final String AUDIO_SELECTOR_BALANCE_ISCP = "SLA06";

/**
 * ISCP Command: SLA05
 * sets COAX/OPT
 */
public static final String AUDIO_SELECTOR_COAX = "audio-selector::coax";
// public static final String AUDIO_SELECTOR_COAX_ISCP = "SLA05";

/**
 * ISCP Command: SLA04
 * sets HDMI
 */
public static final String AUDIO_SELECTOR_HDMI = "audio-selector::hdmi";
// public static final String AUDIO_SELECTOR_HDMI_ISCP = "SLA04";

/**
 * ISCP Command: SLA03
 * sets iLINK
 */
public static final String AUDIO_SELECTOR_ILINK = "audio-selector::ilink";
// public static final String AUDIO_SELECTOR_ILINK_ISCP = "SLA03";

/**
 * ISCP Command: SLA01
 * sets MULTI-CHANNEL
 */
public static final String AUDIO_SELECTOR_MULTI_CHANNEL = "audio-selector::multi-channel";
// public static final String AUDIO_SELECTOR_MULTI_CHANNEL_ISCP = "SLA01";

/**
 * ISCP Command: SLAQSTN
 * gets The Audio Selector Status
 */
public static final String AUDIO_SELECTOR_QUERY = "audio-selector::query";
// public static final String AUDIO_SELECTOR_QUERY_ISCP = "SLAQSTN";

/**
 * ISCP Command: SLAUP
 * sets Audio Selector Wrap-Around Up
 */
public static final String AUDIO_SELECTOR_UP = "audio-selector::up";
// public static final String AUDIO_SELECTOR_UP_ISCP = "SLAUP";

/**
 * CommandBlock
 * ISCP Command prefix: ADY
 * Audyssey 2EQ/MultEQ/MultEQ XT
 */
public static final String AUDYSSEY_2EQ_MULTEQ_MULTEQ_XT = "audyssey-2eq-multeq-multeq-xt";
public static final String AUDYSSEY_2EQ_MULTEQ_MULTEQ_XT_ISCP = "ADY";

/**
 * ISCP Command: ADY02
 * sets Audyssey 2EQ/MultEQ/MultEQ XT Music
 */
public static final String AUDYSSEY_2EQ_MULTEQ_MULTEQ_XT_MUSIC = "audyssey-2eq-multeq-multeq-xt::music";
// public static final String AUDYSSEY_2EQ_MULTEQ_MULTEQ_XT_MUSIC_ISCP = "ADY02";

/**
 * ISCP Command: ADY00
 * sets Audyssey 2EQ/MultEQ/MultEQ XT Off
 */
public static final String AUDYSSEY_2EQ_MULTEQ_MULTEQ_XT_OFF = "audyssey-2eq-multeq-multeq-xt::off";
// public static final String AUDYSSEY_2EQ_MULTEQ_MULTEQ_XT_OFF_ISCP = "ADY00";

/**
 * ISCP Command: ADY01
 * sets Audyssey 2EQ/MultEQ/MultEQ XT On/Movie
 */
public static final String AUDYSSEY_2EQ_MULTEQ_MULTEQ_XT_ON = "audyssey-2eq-multeq-multeq-xt::on";
// public static final String AUDYSSEY_2EQ_MULTEQ_MULTEQ_XT_ON_ISCP = "ADY01";

/**
 * ISCP Command: ADYQSTN
 * gets The Audyssey 2EQ/MultEQ/MultEQ XT State
 */
public static final String AUDYSSEY_2EQ_MULTEQ_MULTEQ_XT_QUERY = "audyssey-2eq-multeq-multeq-xt::query";
// public static final String AUDYSSEY_2EQ_MULTEQ_MULTEQ_XT_QUERY_ISCP = "ADYQSTN";

/**
 * ISCP Command: ADYUP
 * sets Audyssey 2EQ/MultEQ/MultEQ XT State Wrap-Around Up
 */
public static final String AUDYSSEY_2EQ_MULTEQ_MULTEQ_XT_UP = "audyssey-2eq-multeq-multeq-xt::up";
// public static final String AUDYSSEY_2EQ_MULTEQ_MULTEQ_XT_UP_ISCP = "ADYUP";

/**
 * CommandBlock
 * ISCP Command prefix: ADQ
 * Audyssey Dynamic EQ
 */
public static final String AUDYSSEY_DYNAMIC_EQ = "audyssey-dynamic-eq";
public static final String AUDYSSEY_DYNAMIC_EQ_ISCP = "ADQ";

/**
 * ISCP Command: ADQ00
 * sets Audyssey Dynamic EQ Off
 */
public static final String AUDYSSEY_DYNAMIC_EQ_OFF = "audyssey-dynamic-eq::off";
// public static final String AUDYSSEY_DYNAMIC_EQ_OFF_ISCP = "ADQ00";

/**
 * ISCP Command: ADQ01
 * sets Audyssey Dynamic EQ On
 */
public static final String AUDYSSEY_DYNAMIC_EQ_ON = "audyssey-dynamic-eq::on";
// public static final String AUDYSSEY_DYNAMIC_EQ_ON_ISCP = "ADQ01";

/**
 * ISCP Command: ADQQSTN
 * gets The Audyssey Dynamic EQ State
 */
public static final String AUDYSSEY_DYNAMIC_EQ_QUERY = "audyssey-dynamic-eq::query";
// public static final String AUDYSSEY_DYNAMIC_EQ_QUERY_ISCP = "ADQQSTN";

/**
 * ISCP Command: ADQUP
 * sets Audyssey Dynamic EQ State Wrap-Around Up
 */
public static final String AUDYSSEY_DYNAMIC_EQ_UP = "audyssey-dynamic-eq::up";
// public static final String AUDYSSEY_DYNAMIC_EQ_UP_ISCP = "ADQUP";

/**
 * CommandBlock
 * ISCP Command prefix: ADV
 * Audyssey Dynamic Volume
 */
public static final String AUDYSSEY_DYNAMIC_VOLUME = "audyssey-dynamic-volume";
public static final String AUDYSSEY_DYNAMIC_VOLUME_ISCP = "ADV";

/**
 * ISCP Command: ADV03
 * sets Audyssey Dynamic Volume Heavy
 */
public static final String AUDYSSEY_DYNAMIC_VOLUME_HEAVY = "audyssey-dynamic-volume::heavy";
// public static final String AUDYSSEY_DYNAMIC_VOLUME_HEAVY_ISCP = "ADV03";

/**
 * ISCP Command: ADV01
 * sets Audyssey Dynamic Volume Light
 */
public static final String AUDYSSEY_DYNAMIC_VOLUME_LIGHT = "audyssey-dynamic-volume::light";
// public static final String AUDYSSEY_DYNAMIC_VOLUME_LIGHT_ISCP = "ADV01";

/**
 * ISCP Command: ADV02
 * sets Audyssey Dynamic Volume Medium
 */
public static final String AUDYSSEY_DYNAMIC_VOLUME_MEDIUM = "audyssey-dynamic-volume::medium";
// public static final String AUDYSSEY_DYNAMIC_VOLUME_MEDIUM_ISCP = "ADV02";

/**
 * ISCP Command: ADV00
 * sets Audyssey Dynamic Volume Off
 */
public static final String AUDYSSEY_DYNAMIC_VOLUME_OFF = "audyssey-dynamic-volume::off";
// public static final String AUDYSSEY_DYNAMIC_VOLUME_OFF_ISCP = "ADV00";

/**
 * ISCP Command: ADVQSTN
 * gets The Audyssey Dynamic Volume State
 */
public static final String AUDYSSEY_DYNAMIC_VOLUME_QUERY = "audyssey-dynamic-volume::query";
// public static final String AUDYSSEY_DYNAMIC_VOLUME_QUERY_ISCP = "ADVQSTN";

/**
 * ISCP Command: ADVUP
 * sets Audyssey Dynamic Volume State Wrap-Around Up
 */
public static final String AUDYSSEY_DYNAMIC_VOLUME_UP = "audyssey-dynamic-volume::up";
// public static final String AUDYSSEY_DYNAMIC_VOLUME_UP_ISCP = "ADVUP";

/**
 * CommandBlock
 * ISCP Command prefix: CCD
 * CD Player Operation Command
 */
public static final String CD_PLAYER = "cd-player";
public static final String CD_PLAYER_ISCP = "CCD";

/**
 * ISCP Command: CCD0
 * 0.0
 */
public static final String CD_PLAYER_0 = "cd-player::0";
// public static final String CD_PLAYER_0_ISCP = "CCD0";

/**
 * ISCP Command: CCD1
 * 1.0
 */
public static final String CD_PLAYER_1 = "cd-player::1";
// public static final String CD_PLAYER_1_ISCP = "CCD1";

/**
 * ISCP Command: CCD+10
 * +10
 */
public static final String CD_PLAYER_10 = "cd-player::10";
// public static final String CD_PLAYER_10_ISCP = "CCD+10";

/**
 * ISCP Command: CCD2
 * 2.0
 */
public static final String CD_PLAYER_2 = "cd-player::2";
// public static final String CD_PLAYER_2_ISCP = "CCD2";

/**
 * ISCP Command: CCD3
 * 3.0
 */
public static final String CD_PLAYER_3 = "cd-player::3";
// public static final String CD_PLAYER_3_ISCP = "CCD3";

/**
 * ISCP Command: CCD4
 * 4.0
 */
public static final String CD_PLAYER_4 = "cd-player::4";
// public static final String CD_PLAYER_4_ISCP = "CCD4";

/**
 * ISCP Command: CCD5
 * 5.0
 */
public static final String CD_PLAYER_5 = "cd-player::5";
// public static final String CD_PLAYER_5_ISCP = "CCD5";

/**
 * ISCP Command: CCD6
 * 6.0
 */
public static final String CD_PLAYER_6 = "cd-player::6";
// public static final String CD_PLAYER_6_ISCP = "CCD6";

/**
 * ISCP Command: CCD7
 * 7.0
 */
public static final String CD_PLAYER_7 = "cd-player::7";
// public static final String CD_PLAYER_7_ISCP = "CCD7";

/**
 * ISCP Command: CCD8
 * 8.0
 */
public static final String CD_PLAYER_8 = "cd-player::8";
// public static final String CD_PLAYER_8_ISCP = "CCD8";

/**
 * ISCP Command: CCD9
 * 9.0
 */
public static final String CD_PLAYER_9 = "cd-player::9";
// public static final String CD_PLAYER_9_ISCP = "CCD9";

/**
 * ISCP Command: CCDCLEAR
 * CLEAR
 */
public static final String CD_PLAYER_CLEAR = "cd-player::clear";
// public static final String CD_PLAYER_CLEAR_ISCP = "CCDCLEAR";

/**
 * ISCP Command: CCDD.MODE
 * D.MODE
 */
public static final String CD_PLAYER_D_MODE = "cd-player::d-mode";
// public static final String CD_PLAYER_D_MODE_ISCP = "CCDD.MODE";

/**
 * ISCP Command: CCDD.SKIP
 * DISC +
 */
public static final String CD_PLAYER_D_SKIP = "cd-player::d-skip";
// public static final String CD_PLAYER_D_SKIP_ISCP = "CCDD.SKIP";

/**
 * ISCP Command: CCDDISC.F
 * DISC +
 */
public static final String CD_PLAYER_DISC_F = "cd-player::disc-f";
// public static final String CD_PLAYER_DISC_F_ISCP = "CCDDISC.F";

/**
 * ISCP Command: CCDDISC.R
 * DISC -
 */
public static final String CD_PLAYER_DISC_R = "cd-player::disc-r";
// public static final String CD_PLAYER_DISC_R_ISCP = "CCDDISC.R";

/**
 * ISCP Command: CCDDISC1
 * DISC1
 */
public static final String CD_PLAYER_DISC1 = "cd-player::disc1";
// public static final String CD_PLAYER_DISC1_ISCP = "CCDDISC1";

/**
 * ISCP Command: CCDDISC2
 * DISC2
 */
public static final String CD_PLAYER_DISC2 = "cd-player::disc2";
// public static final String CD_PLAYER_DISC2_ISCP = "CCDDISC2";

/**
 * ISCP Command: CCDDISC3
 * DISC3
 */
public static final String CD_PLAYER_DISC3 = "cd-player::disc3";
// public static final String CD_PLAYER_DISC3_ISCP = "CCDDISC3";

/**
 * ISCP Command: CCDDISC4
 * DISC4
 */
public static final String CD_PLAYER_DISC4 = "cd-player::disc4";
// public static final String CD_PLAYER_DISC4_ISCP = "CCDDISC4";

/**
 * ISCP Command: CCDDISC5
 * DISC5
 */
public static final String CD_PLAYER_DISC5 = "cd-player::disc5";
// public static final String CD_PLAYER_DISC5_ISCP = "CCDDISC5";

/**
 * ISCP Command: CCDDISC6
 * DISC6
 */
public static final String CD_PLAYER_DISC6 = "cd-player::disc6";
// public static final String CD_PLAYER_DISC6_ISCP = "CCDDISC6";

/**
 * ISCP Command: CCDDISP
 * DISPLAY
 */
public static final String CD_PLAYER_DISP = "cd-player::disp";
// public static final String CD_PLAYER_DISP_ISCP = "CCDDISP";

/**
 * ISCP Command: CCDFF
 * FF >>
 */
public static final String CD_PLAYER_FF = "cd-player::ff";
// public static final String CD_PLAYER_FF_ISCP = "CCDFF";

/**
 * ISCP Command: CCDMEMORY
 * MEMORY
 */
public static final String CD_PLAYER_MEMORY = "cd-player::memory";
// public static final String CD_PLAYER_MEMORY_ISCP = "CCDMEMORY";

/**
 * ISCP Command: CCDOP/CL
 * OPEN/CLOSE
 */
public static final String CD_PLAYER_OP_CL = "cd-player::op-cl";
// public static final String CD_PLAYER_OP_CL_ISCP = "CCDOP/CL";

/**
 * ISCP Command: CCDPAUSE
 * PAUSE
 */
public static final String CD_PLAYER_PAUSE = "cd-player::pause";
// public static final String CD_PLAYER_PAUSE_ISCP = "CCDPAUSE";

/**
 * ISCP Command: CCDPLAY
 * PLAY
 */
public static final String CD_PLAYER_PLAY = "cd-player::play";
// public static final String CD_PLAYER_PLAY_ISCP = "CCDPLAY";

/**
 * ISCP Command: CCDPON
 * POWER ON
 */
public static final String CD_PLAYER_PON = "cd-player::pon";
// public static final String CD_PLAYER_PON_ISCP = "CCDPON";

/**
 * ISCP Command: CCDPOWER
 * POWER ON/OFF
 */
public static final String CD_PLAYER_POWER = "cd-player::power";
// public static final String CD_PLAYER_POWER_ISCP = "CCDPOWER";

/**
 * ISCP Command: CCDRANDOM
 * RANDOM
 */
public static final String CD_PLAYER_RANDOM = "cd-player::random";
// public static final String CD_PLAYER_RANDOM_ISCP = "CCDRANDOM";

/**
 * ISCP Command: CCDREPEAT
 * REPEAT
 */
public static final String CD_PLAYER_REPEAT = "cd-player::repeat";
// public static final String CD_PLAYER_REPEAT_ISCP = "CCDREPEAT";

/**
 * ISCP Command: CCDREW
 * REW <<
 */
public static final String CD_PLAYER_REW = "cd-player::rew";
// public static final String CD_PLAYER_REW_ISCP = "CCDREW";

/**
 * ISCP Command: CCDSKIP.F
 * >>I
 */
public static final String CD_PLAYER_SKIP_F = "cd-player::skip-f";
// public static final String CD_PLAYER_SKIP_F_ISCP = "CCDSKIP.F";

/**
 * ISCP Command: CCDSKIP.R
 * I<<
 */
public static final String CD_PLAYER_SKIP_R = "cd-player::skip-r";
// public static final String CD_PLAYER_SKIP_R_ISCP = "CCDSKIP.R";

/**
 * ISCP Command: CCDSTBY
 * STANDBY
 */
public static final String CD_PLAYER_STBY = "cd-player::stby";
// public static final String CD_PLAYER_STBY_ISCP = "CCDSTBY";

/**
 * ISCP Command: CCDSTOP
 * STOP
 */
public static final String CD_PLAYER_STOP = "cd-player::stop";
// public static final String CD_PLAYER_STOP_ISCP = "CCDSTOP";

/**
 * ISCP Command: CCDTRACK
 * TRACK+
 */
public static final String CD_PLAYER_TRACK = "cd-player::track";
// public static final String CD_PLAYER_TRACK_ISCP = "CCDTRACK";

/**
 * CommandBlock
 * ISCP Command prefix: CCR
 * CD-R Recorder Operation Command
 */
public static final String CD_R_RECORDER = "cd-r-recorder";
public static final String CD_R_RECORDER_ISCP = "CCR";

/**
 * ISCP Command: CCR1
 * 1.0
 */
public static final String CD_R_RECORDER_1 = "cd-r-recorder::1";
// public static final String CD_R_RECORDER_1_ISCP = "CCR1";

/**
 * ISCP Command: CCR10/0
 * 10/0
 */
public static final String CD_R_RECORDER_10_0 = "cd-r-recorder::10-0";
// public static final String CD_R_RECORDER_10_0_ISCP = "CCR10/0";

/**
 * ISCP Command: CCR2
 * 2.0
 */
public static final String CD_R_RECORDER_2 = "cd-r-recorder::2";
// public static final String CD_R_RECORDER_2_ISCP = "CCR2";

/**
 * ISCP Command: CCR3
 * 3.0
 */
public static final String CD_R_RECORDER_3 = "cd-r-recorder::3";
// public static final String CD_R_RECORDER_3_ISCP = "CCR3";

/**
 * ISCP Command: CCR4
 * 4.0
 */
public static final String CD_R_RECORDER_4 = "cd-r-recorder::4";
// public static final String CD_R_RECORDER_4_ISCP = "CCR4";

/**
 * ISCP Command: CCR5
 * 5.0
 */
public static final String CD_R_RECORDER_5 = "cd-r-recorder::5";
// public static final String CD_R_RECORDER_5_ISCP = "CCR5";

/**
 * ISCP Command: CCR6
 * 6.0
 */
public static final String CD_R_RECORDER_6 = "cd-r-recorder::6";
// public static final String CD_R_RECORDER_6_ISCP = "CCR6";

/**
 * ISCP Command: CCR7
 * 7.0
 */
public static final String CD_R_RECORDER_7 = "cd-r-recorder::7";
// public static final String CD_R_RECORDER_7_ISCP = "CCR7";

/**
 * ISCP Command: CCR8
 * 8.0
 */
public static final String CD_R_RECORDER_8 = "cd-r-recorder::8";
// public static final String CD_R_RECORDER_8_ISCP = "CCR8";

/**
 * ISCP Command: CCR9
 * 9.0
 */
public static final String CD_R_RECORDER_9 = "cd-r-recorder::9";
// public static final String CD_R_RECORDER_9_ISCP = "CCR9";

/**
 * ISCP Command: CCRCLEAR
 * CLEAR
 */
public static final String CD_R_RECORDER_CLEAR = "cd-r-recorder::clear";
// public static final String CD_R_RECORDER_CLEAR_ISCP = "CCRCLEAR";

/**
 * ISCP Command: CCRDISP
 * DISPLAY
 */
public static final String CD_R_RECORDER_DISP = "cd-r-recorder::disp";
// public static final String CD_R_RECORDER_DISP_ISCP = "CCRDISP";

/**
 * ISCP Command: CCRFF
 * FF
 */
public static final String CD_R_RECORDER_FF = "cd-r-recorder::ff";
// public static final String CD_R_RECORDER_FF_ISCP = "CCRFF";

/**
 * ISCP Command: CCRMEMORY
 * MEMORY
 */
public static final String CD_R_RECORDER_MEMORY = "cd-r-recorder::memory";
// public static final String CD_R_RECORDER_MEMORY_ISCP = "CCRMEMORY";

/**
 * ISCP Command: CCRnn/nnn
 * --/---
 */
public static final String CD_R_RECORDER_NULL = "cd-r-recorder::null";
// public static final String CD_R_RECORDER_NULL_ISCP = "CCRnn/nnn";

/**
 * ISCP Command: CCROP/CL
 * OPEN/CLOSE
 */
public static final String CD_R_RECORDER_OP_CL = "cd-r-recorder::op-cl";
// public static final String CD_R_RECORDER_OP_CL_ISCP = "CCROP/CL";

/**
 * ISCP Command: CCRP.MODE
 * PLAY MODE
 */
public static final String CD_R_RECORDER_P_MODE = "cd-r-recorder::p-mode";
// public static final String CD_R_RECORDER_P_MODE_ISCP = "CCRP.MODE";

/**
 * ISCP Command: CCRPAUSE
 * PAUSE
 */
public static final String CD_R_RECORDER_PAUSE = "cd-r-recorder::pause";
// public static final String CD_R_RECORDER_PAUSE_ISCP = "CCRPAUSE";

/**
 * ISCP Command: CCRPLAY
 * PLAY
 */
public static final String CD_R_RECORDER_PLAY = "cd-r-recorder::play";
// public static final String CD_R_RECORDER_PLAY_ISCP = "CCRPLAY";

/**
 * ISCP Command: CCRPOWER
 * POWER ON/OFF
 */
public static final String CD_R_RECORDER_POWER = "cd-r-recorder::power";
// public static final String CD_R_RECORDER_POWER_ISCP = "CCRPOWER";

/**
 * ISCP Command: CCRRANDOM
 * RANDOM
 */
public static final String CD_R_RECORDER_RANDOM = "cd-r-recorder::random";
// public static final String CD_R_RECORDER_RANDOM_ISCP = "CCRRANDOM";

/**
 * ISCP Command: CCRREC
 * REC
 */
public static final String CD_R_RECORDER_REC = "cd-r-recorder::rec";
// public static final String CD_R_RECORDER_REC_ISCP = "CCRREC";

/**
 * ISCP Command: CCRREPEAT
 * REPEAT
 */
public static final String CD_R_RECORDER_REPEAT = "cd-r-recorder::repeat";
// public static final String CD_R_RECORDER_REPEAT_ISCP = "CCRREPEAT";

/**
 * ISCP Command: CCRREW
 * REW
 */
public static final String CD_R_RECORDER_REW = "cd-r-recorder::rew";
// public static final String CD_R_RECORDER_REW_ISCP = "CCRREW";

/**
 * ISCP Command: CCRSCROLL
 * SCROLL
 */
public static final String CD_R_RECORDER_SCROLL = "cd-r-recorder::scroll";
// public static final String CD_R_RECORDER_SCROLL_ISCP = "CCRSCROLL";

/**
 * ISCP Command: CCRSKIP.F
 * >>I
 */
public static final String CD_R_RECORDER_SKIP_F = "cd-r-recorder::skip-f";
// public static final String CD_R_RECORDER_SKIP_F_ISCP = "CCRSKIP.F";

/**
 * ISCP Command: CCRSKIP.R
 * I<<
 */
public static final String CD_R_RECORDER_SKIP_R = "cd-r-recorder::skip-r";
// public static final String CD_R_RECORDER_SKIP_R_ISCP = "CCRSKIP.R";

/**
 * ISCP Command: CCRSTBY
 * STANDBY
 */
public static final String CD_R_RECORDER_STBY = "cd-r-recorder::stby";
// public static final String CD_R_RECORDER_STBY_ISCP = "CCRSTBY";

/**
 * ISCP Command: CCRSTOP
 * STOP
 */
public static final String CD_R_RECORDER_STOP = "cd-r-recorder::stop";
// public static final String CD_R_RECORDER_STOP_ISCP = "CCRSTOP";

/**
 * CommandBlock
 * ISCP Command prefix: CTL
 * Center (temporary) Level Command
 */
public static final String CENTER_TEMPORARY_LEVEL = "center-temporary-level";
public static final String CENTER_TEMPORARY_LEVEL_ISCP = "CTL";

/**
 * ISCP Command: CTLDOWN
 * LEVEL ? KEY
 */
public static final String CENTER_TEMPORARY_LEVEL_DOWN = "center-temporary-level::down";
// public static final String CENTER_TEMPORARY_LEVEL_DOWN_ISCP = "CTLDOWN";

/**
 * ISCP Command: CTLQSTN
 * gets the Subwoofer Level
 */
public static final String CENTER_TEMPORARY_LEVEL_QUERY = "center-temporary-level::query";
// public static final String CENTER_TEMPORARY_LEVEL_QUERY_ISCP = "CTLQSTN";

/**
 * ISCP Command: CTLUP
 * LEVEL + Key
 */
public static final String CENTER_TEMPORARY_LEVEL_UP = "center-temporary-level::up";
// public static final String CENTER_TEMPORARY_LEVEL_UP_ISCP = "CTLUP";

/**
 * CommandBlock
 * ISCP Command prefix: RAS
 * Cinema Filter Command
 */
public static final String CINEMA_FILTER = "cinema-filter";
public static final String CINEMA_FILTER_ISCP = "RAS";

/**
 * ISCP Command: RAS00
 * sets Cinema Filter Off
 */
public static final String CINEMA_FILTER_OFF = "cinema-filter::off";
// public static final String CINEMA_FILTER_OFF_ISCP = "RAS00";

/**
 * ISCP Command: RAS01
 * sets Cinema Filter On
 */
public static final String CINEMA_FILTER_ON = "cinema-filter::on";
// public static final String CINEMA_FILTER_ON_ISCP = "RAS01";

/**
 * ISCP Command: RASQSTN
 * gets The Cinema Filter State
 */
public static final String CINEMA_FILTER_QUERY = "cinema-filter::query";
// public static final String CINEMA_FILTER_QUERY_ISCP = "RASQSTN";

/**
 * ISCP Command: RASUP
 * sets Cinema Filter State Wrap-Around Up
 */
public static final String CINEMA_FILTER_UP = "cinema-filter::up";
// public static final String CINEMA_FILTER_UP_ISCP = "RASUP";

/**
 * CommandBlock
 * ISCP Command prefix: UDD
 * DAB Display Info (Universal Port Dock Only)
 */
public static final String DAB_DISPLAY_INFO = "dab-display-info";
public static final String DAB_DISPLAY_INFO_ISCP = "UDD";

/**
 * ISCP Command: UDDAT
 * gets & display DAB Bitrate & Audio Type
 */
public static final String DAB_DISPLAY_INFO_AT = "dab-display-info::at";
// public static final String DAB_DISPLAY_INFO_AT_ISCP = "UDDAT";

/**
 * ISCP Command: UDDMF
 * gets & display DAB Multicast Band & Freq Info
 */
public static final String DAB_DISPLAY_INFO_MF = "dab-display-info::mf";
// public static final String DAB_DISPLAY_INFO_MF_ISCP = "UDDMF";

/**
 * ISCP Command: UDDMN
 * gets & display DAB Multicast Name
 */
public static final String DAB_DISPLAY_INFO_MN = "dab-display-info::mn";
// public static final String DAB_DISPLAY_INFO_MN_ISCP = "UDDMN";

/**
 * ISCP Command: UDDMF:mmm/nnnn.nnMHz
 * DAB Multiplex Band ID(mmm) & Freq(nnnn.nnMHz) Info
 */
public static final String DAB_DISPLAY_INFO_NULL = "dab-display-info::null";
// public static final String DAB_DISPLAY_INFO_NULL_ISCP = "UDDMF:mmm/nnnn.nnMHz";

/**
 * ISCP Command: UDDPT
 * gets & display DAB Program Info
 */
public static final String DAB_DISPLAY_INFO_PT = "dab-display-info::pt";
// public static final String DAB_DISPLAY_INFO_PT_ISCP = "UDDPT";

/**
 * ISCP Command: UDDUP
 * gets & dispaly DAB Infomation Wrap-Around Up
 */
public static final String DAB_DISPLAY_INFO_UP = "dab-display-info::up";
// public static final String DAB_DISPLAY_INFO_UP_ISCP = "UDDUP";

/**
 * CommandBlock
 * ISCP Command prefix: UPR
 * DAB Preset Command (Universal Port Dock Only)
 */
public static final String DAB_PRESET = "dab-preset";
public static final String DAB_PRESET_ISCP = "UPR";

/**
 * ISCP Command: UPRDOWN
 * sets Preset No. Wrap-Around Down
 */
public static final String DAB_PRESET_DOWN = "dab-preset::down";
// public static final String DAB_PRESET_DOWN_ISCP = "UPRDOWN";

/**
 * ISCP Command: UPRQSTN
 * gets The Preset No.
 */
public static final String DAB_PRESET_QUERY = "dab-preset::query";
// public static final String DAB_PRESET_QUERY_ISCP = "UPRQSTN";

/**
 * ISCP Command: UPRUP
 * sets Preset No. Wrap-Around Up
 */
public static final String DAB_PRESET_UP = "dab-preset::up";
// public static final String DAB_PRESET_UP_ISCP = "UPRUP";

/**
 * CommandBlock
 * ISCP Command prefix: UDS
 * DAB Sation Name (Universal Port Dock Only)
 */
public static final String DAB_SATION_NAME = "dab-sation-name";
public static final String DAB_SATION_NAME_ISCP = "UDS";

/**
 * ISCP Command: UDSnnnnnnnnn
 * Sation Name (9 letters)
 */
public static final String DAB_SATION_NAME_NULL = "dab-sation-name::null";
// public static final String DAB_SATION_NAME_NULL_ISCP = "UDSnnnnnnnnn";

/**
 * ISCP Command: UDSQSTN
 * gets The Tuning Frequency
 */
public static final String DAB_SATION_NAME_QUERY = "dab-sation-name::query";
// public static final String DAB_SATION_NAME_QUERY_ISCP = "UDSQSTN";

/**
 * CommandBlock
 * ISCP Command prefix: CDT
 * DAT Recorder Operation Command
 */
public static final String DAT_RECORDER = "dat-recorder";
public static final String DAT_RECORDER_ISCP = "CDT";

/**
 * ISCP Command: CDTFF
 * FF >>
 */
public static final String DAT_RECORDER_FF = "dat-recorder::ff";
// public static final String DAT_RECORDER_FF_ISCP = "CDTFF";

/**
 * ISCP Command: CDTPLAY
 * PLAY
 */
public static final String DAT_RECORDER_PLAY = "dat-recorder::play";
// public static final String DAT_RECORDER_PLAY_ISCP = "CDTPLAY";

/**
 * ISCP Command: CDTRC/PAU
 * REC/PAUSE
 */
public static final String DAT_RECORDER_RC_PAU = "dat-recorder::rc-pau";
// public static final String DAT_RECORDER_RC_PAU_ISCP = "CDTRC/PAU";

/**
 * ISCP Command: CDTREW
 * REW <<
 */
public static final String DAT_RECORDER_REW = "dat-recorder::rew";
// public static final String DAT_RECORDER_REW_ISCP = "CDTREW";

/**
 * ISCP Command: CDTSKIP.F
 * >>I
 */
public static final String DAT_RECORDER_SKIP_F = "dat-recorder::skip-f";
// public static final String DAT_RECORDER_SKIP_F_ISCP = "CDTSKIP.F";

/**
 * ISCP Command: CDTSKIP.R
 * I<<
 */
public static final String DAT_RECORDER_SKIP_R = "dat-recorder::skip-r";
// public static final String DAT_RECORDER_SKIP_R_ISCP = "CDTSKIP.R";

/**
 * ISCP Command: CDTSTOP
 * STOP
 */
public static final String DAT_RECORDER_STOP = "dat-recorder::stop";
// public static final String DAT_RECORDER_STOP_ISCP = "CDTSTOP";

/**
 * CommandBlock
 * ISCP Command prefix: DIM
 * Dimmer Level Command
 */
public static final String DIMMER_LEVEL = "dimmer-level";
public static final String DIMMER_LEVEL_ISCP = "DIM";

/**
 * ISCP Command: DIM00
 * sets Dimmer Level "Bright"
 */
public static final String DIMMER_LEVEL_BRIGHT = "dimmer-level::bright";
// public static final String DIMMER_LEVEL_BRIGHT_ISCP = "DIM00";

/**
 * ISCP Command: DIM02
 * sets Dimmer Level "Dark"
 */
public static final String DIMMER_LEVEL_DARK = "dimmer-level::dark";
// public static final String DIMMER_LEVEL_DARK_ISCP = "DIM02";

/**
 * ISCP Command: DIMDIM
 * sets Dimmer Level Wrap-Around Up
 */
public static final String DIMMER_LEVEL_DIM = "dimmer-level::dim";
// public static final String DIMMER_LEVEL_DIM_ISCP = "DIMDIM";

/**
 * ISCP Command: DIMQSTN
 * gets The Dimmer Level
 */
public static final String DIMMER_LEVEL_QUERY = "dimmer-level::query";
// public static final String DIMMER_LEVEL_QUERY_ISCP = "DIMQSTN";

/**
 * ISCP Command: DIM03
 * sets Dimmer Level "Shut-Off"
 */
public static final String DIMMER_LEVEL_SHUT_OFF = "dimmer-level::shut-off";
// public static final String DIMMER_LEVEL_SHUT_OFF_ISCP = "DIM03";

/**
 * CommandBlock
 * ISCP Command prefix: DIF
 * Display Mode Command
 */
public static final String DISPLAY_MODE = "display-mode";
public static final String DISPLAY_MODE_ISCP = "DIF";

/**
 * ISCP Command: DIF02
 * Display Digital Format(temporary display)
 */
public static final String DISPLAY_MODE_02 = "display-mode::02";
// public static final String DISPLAY_MODE_02_ISCP = "DIF02";

/**
 * ISCP Command: DIF03
 * Display Video Format(temporary display)
 */
public static final String DISPLAY_MODE_03 = "display-mode::03";
// public static final String DISPLAY_MODE_03_ISCP = "DIF03";

/**
 * ISCP Command: DIFQSTN
 * gets The Display Mode
 */
public static final String DISPLAY_MODE_QUERY = "display-mode::query";
// public static final String DISPLAY_MODE_QUERY_ISCP = "DIFQSTN";

/**
 * ISCP Command: DIF01
 * sets Selector + Listening Mode Display Mode
 */
public static final String DISPLAY_MODE_SELECTOR_LISTENING = "display-mode::selector-listening";
// public static final String DISPLAY_MODE_SELECTOR_LISTENING_ISCP = "DIF01";

/**
 * ISCP Command: DIF00
 * sets Selector + Volume Display Mode
 */
public static final String DISPLAY_MODE_SELECTOR_VOLUME = "display-mode::selector-volume";
// public static final String DISPLAY_MODE_SELECTOR_VOLUME_ISCP = "DIF00";

/**
 * ISCP Command: DIFTG
 * sets Display Mode Wrap-Around Up
 */
public static final String DISPLAY_MODE_TOGGLE = "display-mode::toggle";
// public static final String DISPLAY_MODE_TOGGLE_ISCP = "DIFTG";

/**
 * CommandBlock
 * ISCP Command prefix: DVL
 * Dolby Volume
 */
public static final String DOLBY_VOLUME = "dolby-volume";
public static final String DOLBY_VOLUME_ISCP = "DVL";

/**
 * ISCP Command: DVL03
 * sets Dolby Volume High
 */
public static final String DOLBY_VOLUME_HIGH = "dolby-volume::high";
// public static final String DOLBY_VOLUME_HIGH_ISCP = "DVL03";

/**
 * ISCP Command: DVL01
 * sets Dolby Volume Low/On
 */
public static final String DOLBY_VOLUME_LOW = "dolby-volume::low";
// public static final String DOLBY_VOLUME_LOW_ISCP = "DVL01";

/**
 * ISCP Command: DVL02
 * sets Dolby Volume Mid
 */
public static final String DOLBY_VOLUME_MID = "dolby-volume::mid";
// public static final String DOLBY_VOLUME_MID_ISCP = "DVL02";

/**
 * ISCP Command: DVL00
 * sets Dolby Volume Off
 */
public static final String DOLBY_VOLUME_OFF = "dolby-volume::off";
// public static final String DOLBY_VOLUME_OFF_ISCP = "DVL00";

/**
 * ISCP Command: DVLQSTN
 * gets The Dolby Volume State
 */
public static final String DOLBY_VOLUME_QUERY = "dolby-volume::query";
// public static final String DOLBY_VOLUME_QUERY_ISCP = "DVLQSTN";

/**
 * ISCP Command: DVLUP
 * sets Dolby Volume State Wrap-Around Up
 */
public static final String DOLBY_VOLUME_UP = "dolby-volume::up";
// public static final String DOLBY_VOLUME_UP_ISCP = "DVLUP";

/**
 * CommandBlock
 * ISCP Command prefix: CDV
 * DVD Player Operation Command (via RIHD only after TX-NR509)
 */
public static final String DVD_PLAYER = "dvd-player";
public static final String DVD_PLAYER_ISCP = "CDV";

/**
 * ISCP Command: CDV0
 * 0.0
 */
public static final String DVD_PLAYER_0 = "dvd-player::0";
// public static final String DVD_PLAYER_0_ISCP = "CDV0";

/**
 * ISCP Command: CDV1
 * 1.0
 */
public static final String DVD_PLAYER_1 = "dvd-player::1";
// public static final String DVD_PLAYER_1_ISCP = "CDV1";

/**
 * ISCP Command: CDV10
 * 10.0
 */
public static final String DVD_PLAYER_10 = "dvd-player::10";
// public static final String DVD_PLAYER_10_ISCP = "CDV10";

/**
 * ISCP Command: CDV2
 * 2.0
 */
public static final String DVD_PLAYER_2 = "dvd-player::2";
// public static final String DVD_PLAYER_2_ISCP = "CDV2";

/**
 * ISCP Command: CDV3
 * 3.0
 */
public static final String DVD_PLAYER_3 = "dvd-player::3";
// public static final String DVD_PLAYER_3_ISCP = "CDV3";

/**
 * ISCP Command: CDV4
 * 4.0
 */
public static final String DVD_PLAYER_4 = "dvd-player::4";
// public static final String DVD_PLAYER_4_ISCP = "CDV4";

/**
 * ISCP Command: CDV5
 * 5.0
 */
public static final String DVD_PLAYER_5 = "dvd-player::5";
// public static final String DVD_PLAYER_5_ISCP = "CDV5";

/**
 * ISCP Command: CDV6
 * 6.0
 */
public static final String DVD_PLAYER_6 = "dvd-player::6";
// public static final String DVD_PLAYER_6_ISCP = "CDV6";

/**
 * ISCP Command: CDV7
 * 7.0
 */
public static final String DVD_PLAYER_7 = "dvd-player::7";
// public static final String DVD_PLAYER_7_ISCP = "CDV7";

/**
 * ISCP Command: CDV8
 * 8.0
 */
public static final String DVD_PLAYER_8 = "dvd-player::8";
// public static final String DVD_PLAYER_8_ISCP = "CDV8";

/**
 * ISCP Command: CDV9
 * 9.0
 */
public static final String DVD_PLAYER_9 = "dvd-player::9";
// public static final String DVD_PLAYER_9_ISCP = "CDV9";

/**
 * ISCP Command: CDVABR
 * A-B REPEAT
 */
public static final String DVD_PLAYER_ABR = "dvd-player::abr";
// public static final String DVD_PLAYER_ABR_ISCP = "CDVABR";

/**
 * ISCP Command: CDVANGLE
 * ANGLE
 */
public static final String DVD_PLAYER_ANGLE = "dvd-player::angle";
// public static final String DVD_PLAYER_ANGLE_ISCP = "CDVANGLE";

/**
 * ISCP Command: CDVASCTG
 * ASPECT(Toggle)
 */
public static final String DVD_PLAYER_ASCTG = "dvd-player::asctg";
// public static final String DVD_PLAYER_ASCTG_ISCP = "CDVASCTG";

/**
 * ISCP Command: CDVAUDIO
 * AUDIO
 */
public static final String DVD_PLAYER_AUDIO = "dvd-player::audio";
// public static final String DVD_PLAYER_AUDIO_ISCP = "CDVAUDIO";

/**
 * ISCP Command: CDVCDPCD
 * CD CHAIN REPEAT
 */
public static final String DVD_PLAYER_CDPCD = "dvd-player::cdpcd";
// public static final String DVD_PLAYER_CDPCD_ISCP = "CDVCDPCD";

/**
 * ISCP Command: CDVCLEAR
 * CLEAR
 */
public static final String DVD_PLAYER_CLEAR = "dvd-player::clear";
// public static final String DVD_PLAYER_CLEAR_ISCP = "CDVCLEAR";

/**
 * ISCP Command: CDVCONMEM
 * CONDITION MEMORY
 */
public static final String DVD_PLAYER_CONMEM = "dvd-player::conmem";
// public static final String DVD_PLAYER_CONMEM_ISCP = "CDVCONMEM";

/**
 * ISCP Command: CDVDISC.F
 * DISC +
 */
public static final String DVD_PLAYER_DISC_F = "dvd-player::disc-f";
// public static final String DVD_PLAYER_DISC_F_ISCP = "CDVDISC.F";

/**
 * ISCP Command: CDVDISC.R
 * DISC -
 */
public static final String DVD_PLAYER_DISC_R = "dvd-player::disc-r";
// public static final String DVD_PLAYER_DISC_R_ISCP = "CDVDISC.R";

/**
 * ISCP Command: CDVDISC1
 * DISC1
 */
public static final String DVD_PLAYER_DISC1 = "dvd-player::disc1";
// public static final String DVD_PLAYER_DISC1_ISCP = "CDVDISC1";

/**
 * ISCP Command: CDVDISC2
 * DISC2
 */
public static final String DVD_PLAYER_DISC2 = "dvd-player::disc2";
// public static final String DVD_PLAYER_DISC2_ISCP = "CDVDISC2";

/**
 * ISCP Command: CDVDISC3
 * DISC3
 */
public static final String DVD_PLAYER_DISC3 = "dvd-player::disc3";
// public static final String DVD_PLAYER_DISC3_ISCP = "CDVDISC3";

/**
 * ISCP Command: CDVDISC4
 * DISC4
 */
public static final String DVD_PLAYER_DISC4 = "dvd-player::disc4";
// public static final String DVD_PLAYER_DISC4_ISCP = "CDVDISC4";

/**
 * ISCP Command: CDVDISC5
 * DISC5
 */
public static final String DVD_PLAYER_DISC5 = "dvd-player::disc5";
// public static final String DVD_PLAYER_DISC5_ISCP = "CDVDISC5";

/**
 * ISCP Command: CDVDISC6
 * DISC6
 */
public static final String DVD_PLAYER_DISC6 = "dvd-player::disc6";
// public static final String DVD_PLAYER_DISC6_ISCP = "CDVDISC6";

/**
 * ISCP Command: CDVDISP
 * DISPLAY
 */
public static final String DVD_PLAYER_DISP = "dvd-player::disp";
// public static final String DVD_PLAYER_DISP_ISCP = "CDVDISP";

/**
 * ISCP Command: CDVDOWN
 * DOWN
 */
public static final String DVD_PLAYER_DOWN = "dvd-player::down";
// public static final String DVD_PLAYER_DOWN_ISCP = "CDVDOWN";

/**
 * ISCP Command: CDVENTER
 * ENTER
 */
public static final String DVD_PLAYER_ENTER = "dvd-player::enter";
// public static final String DVD_PLAYER_ENTER_ISCP = "CDVENTER";

/**
 * ISCP Command: CDVFF
 * FF >>
 */
public static final String DVD_PLAYER_FF = "dvd-player::ff";
// public static final String DVD_PLAYER_FF_ISCP = "CDVFF";

/**
 * ISCP Command: CDVFOLDDN
 * FOLDER DOWN
 */
public static final String DVD_PLAYER_FOLDDN = "dvd-player::folddn";
// public static final String DVD_PLAYER_FOLDDN_ISCP = "CDVFOLDDN";

/**
 * ISCP Command: CDVFOLDUP
 * FOLDER UP
 */
public static final String DVD_PLAYER_FOLDUP = "dvd-player::foldup";
// public static final String DVD_PLAYER_FOLDUP_ISCP = "CDVFOLDUP";

/**
 * ISCP Command: CDVFUNMEM
 * FUNCTION MEMORY
 */
public static final String DVD_PLAYER_FUNMEM = "dvd-player::funmem";
// public static final String DVD_PLAYER_FUNMEM_ISCP = "CDVFUNMEM";

/**
 * ISCP Command: CDVINIT
 * Return to Factory Settings
 */
public static final String DVD_PLAYER_INIT = "dvd-player::init";
// public static final String DVD_PLAYER_INIT_ISCP = "CDVINIT";

/**
 * ISCP Command: CDVLASTPLAY
 * LAST PLAY
 */
public static final String DVD_PLAYER_LASTPLAY = "dvd-player::lastplay";
// public static final String DVD_PLAYER_LASTPLAY_ISCP = "CDVLASTPLAY";

/**
 * ISCP Command: CDVLEFT
 * LEFT
 */
public static final String DVD_PLAYER_LEFT = "dvd-player::left";
// public static final String DVD_PLAYER_LEFT_ISCP = "CDVLEFT";

/**
 * ISCP Command: CDVMEMORY
 * MEMORY
 */
public static final String DVD_PLAYER_MEMORY = "dvd-player::memory";
// public static final String DVD_PLAYER_MEMORY_ISCP = "CDVMEMORY";

/**
 * ISCP Command: CDVMENU
 * MENU
 */
public static final String DVD_PLAYER_MENU = "dvd-player::menu";
// public static final String DVD_PLAYER_MENU_ISCP = "CDVMENU";

/**
 * ISCP Command: CDVMSPDN
 * MULTI SPEED DOWN
 */
public static final String DVD_PLAYER_MSPDN = "dvd-player::mspdn";
// public static final String DVD_PLAYER_MSPDN_ISCP = "CDVMSPDN";

/**
 * ISCP Command: CDVMSPUP
 * MULTI SPEED UP
 */
public static final String DVD_PLAYER_MSPUP = "dvd-player::mspup";
// public static final String DVD_PLAYER_MSPUP_ISCP = "CDVMSPUP";

/**
 * ISCP Command: CDVOP/CL
 * OPEN/CLOSE
 */
public static final String DVD_PLAYER_OP_CL = "dvd-player::op-cl";
// public static final String DVD_PLAYER_OP_CL_ISCP = "CDVOP/CL";

/**
 * ISCP Command: CDVP.MODE
 * PLAY MODE
 */
public static final String DVD_PLAYER_P_MODE = "dvd-player::p-mode";
// public static final String DVD_PLAYER_P_MODE_ISCP = "CDVP.MODE";

/**
 * ISCP Command: CDVPAUSE
 * PAUSE
 */
public static final String DVD_PLAYER_PAUSE = "dvd-player::pause";
// public static final String DVD_PLAYER_PAUSE_ISCP = "CDVPAUSE";

/**
 * ISCP Command: CDVPCT
 * PICTURE CONTROL
 */
public static final String DVD_PLAYER_PCT = "dvd-player::pct";
// public static final String DVD_PLAYER_PCT_ISCP = "CDVPCT";

/**
 * ISCP Command: CDVPLAY
 * PLAY
 */
public static final String DVD_PLAYER_PLAY = "dvd-player::play";
// public static final String DVD_PLAYER_PLAY_ISCP = "CDVPLAY";

/**
 * ISCP Command: CDVPOWER
 * POWER ON/OFF
 */
public static final String DVD_PLAYER_POWER = "dvd-player::power";
// public static final String DVD_PLAYER_POWER_ISCP = "CDVPOWER";

/**
 * ISCP Command: CDVPROGRE
 * PROGRESSIVE
 */
public static final String DVD_PLAYER_PROGRE = "dvd-player::progre";
// public static final String DVD_PLAYER_PROGRE_ISCP = "CDVPROGRE";

/**
 * ISCP Command: CDVPWROFF
 * POWER OFF
 */
public static final String DVD_PLAYER_PWROFF = "dvd-player::pwroff";
// public static final String DVD_PLAYER_PWROFF_ISCP = "CDVPWROFF";

/**
 * ISCP Command: CDVPWRON
 * POWER ON
 */
public static final String DVD_PLAYER_PWRON = "dvd-player::pwron";
// public static final String DVD_PLAYER_PWRON_ISCP = "CDVPWRON";

/**
 * ISCP Command: CDVRANDOM
 * RANDOM
 */
public static final String DVD_PLAYER_RANDOM = "dvd-player::random";
// public static final String DVD_PLAYER_RANDOM_ISCP = "CDVRANDOM";

/**
 * ISCP Command: CDVREPEAT
 * REPEAT
 */
public static final String DVD_PLAYER_REPEAT = "dvd-player::repeat";
// public static final String DVD_PLAYER_REPEAT_ISCP = "CDVREPEAT";

/**
 * ISCP Command: CDVRETURN
 * RETURN
 */
public static final String DVD_PLAYER_RETURN = "dvd-player::return";
// public static final String DVD_PLAYER_RETURN_ISCP = "CDVRETURN";

/**
 * ISCP Command: CDVREW
 * REW <<
 */
public static final String DVD_PLAYER_REW = "dvd-player::rew";
// public static final String DVD_PLAYER_REW_ISCP = "CDVREW";

/**
 * ISCP Command: CDVRIGHT
 * RIGHT
 */
public static final String DVD_PLAYER_RIGHT = "dvd-player::right";
// public static final String DVD_PLAYER_RIGHT_ISCP = "CDVRIGHT";

/**
 * ISCP Command: CDVRSCTG
 * RESOLUTION(Toggle)
 */
public static final String DVD_PLAYER_RSCTG = "dvd-player::rsctg";
// public static final String DVD_PLAYER_RSCTG_ISCP = "CDVRSCTG";

/**
 * ISCP Command: CDVSEARCH
 * SEARCH
 */
public static final String DVD_PLAYER_SEARCH = "dvd-player::search";
// public static final String DVD_PLAYER_SEARCH_ISCP = "CDVSEARCH";

/**
 * ISCP Command: CDVSETUP
 * SETUP
 */
public static final String DVD_PLAYER_SETUP = "dvd-player::setup";
// public static final String DVD_PLAYER_SETUP_ISCP = "CDVSETUP";

/**
 * ISCP Command: CDVSKIP.F
 * >>I
 */
public static final String DVD_PLAYER_SKIP_F = "dvd-player::skip-f";
// public static final String DVD_PLAYER_SKIP_F_ISCP = "CDVSKIP.F";

/**
 * ISCP Command: CDVSKIP.R
 * I<<
 */
public static final String DVD_PLAYER_SKIP_R = "dvd-player::skip-r";
// public static final String DVD_PLAYER_SKIP_R_ISCP = "CDVSKIP.R";

/**
 * ISCP Command: CDVSLOW.F
 * SLOW
 */
public static final String DVD_PLAYER_SLOW_F = "dvd-player::slow-f";
// public static final String DVD_PLAYER_SLOW_F_ISCP = "CDVSLOW.F";

/**
 * ISCP Command: CDVSLOW.R
 * SLOW BACK
 */
public static final String DVD_PLAYER_SLOW_R = "dvd-player::slow-r";
// public static final String DVD_PLAYER_SLOW_R_ISCP = "CDVSLOW.R";

/**
 * ISCP Command: CDVSTEP.F
 * STEP
 */
public static final String DVD_PLAYER_STEP_F = "dvd-player::step-f";
// public static final String DVD_PLAYER_STEP_F_ISCP = "CDVSTEP.F";

/**
 * ISCP Command: CDVSTEP.R
 * STEP BACK
 */
public static final String DVD_PLAYER_STEP_R = "dvd-player::step-r";
// public static final String DVD_PLAYER_STEP_R_ISCP = "CDVSTEP.R";

/**
 * ISCP Command: CDVSTOP
 * STOP
 */
public static final String DVD_PLAYER_STOP = "dvd-player::stop";
// public static final String DVD_PLAYER_STOP_ISCP = "CDVSTOP";

/**
 * ISCP Command: CDVSUBTITLE
 * SUBTITLE
 */
public static final String DVD_PLAYER_SUBTITLE = "dvd-player::subtitle";
// public static final String DVD_PLAYER_SUBTITLE_ISCP = "CDVSUBTITLE";

/**
 * ISCP Command: CDVSUBTON/OFF
 * SUBTITLE ON/OFF
 */
public static final String DVD_PLAYER_SUBTON_OFF = "dvd-player::subton-off";
// public static final String DVD_PLAYER_SUBTON_OFF_ISCP = "CDVSUBTON/OFF";

/**
 * ISCP Command: CDVTOPMENU
 * TOPMENU
 */
public static final String DVD_PLAYER_TOPMENU = "dvd-player::topmenu";
// public static final String DVD_PLAYER_TOPMENU_ISCP = "CDVTOPMENU";

/**
 * ISCP Command: CDVUP
 * UP
 */
public static final String DVD_PLAYER_UP = "dvd-player::up";
// public static final String DVD_PLAYER_UP_ISCP = "CDVUP";

/**
 * ISCP Command: CDVVDOFF
 * VIDEO ON/OFF
 */
public static final String DVD_PLAYER_VDOFF = "dvd-player::vdoff";
// public static final String DVD_PLAYER_VDOFF_ISCP = "CDVVDOFF";

/**
 * ISCP Command: CDVZOOMDN
 * ZOOM DOWN
 */
public static final String DVD_PLAYER_ZOOMDN = "dvd-player::zoomdn";
// public static final String DVD_PLAYER_ZOOMDN_ISCP = "CDVZOOMDN";

/**
 * ISCP Command: CDVZOOMTG
 * ZOOM
 */
public static final String DVD_PLAYER_ZOOMTG = "dvd-player::zoomtg";
// public static final String DVD_PLAYER_ZOOMTG_ISCP = "CDVZOOMTG";

/**
 * ISCP Command: CDVZOOMUP
 * ZOOM UP
 */
public static final String DVD_PLAYER_ZOOMUP = "dvd-player::zoomup";
// public static final String DVD_PLAYER_ZOOMUP_ISCP = "CDVZOOMUP";

/**
 * CommandBlock
 * ISCP Command prefix: CEQ
 * Graphics Equalizer Operation Command
 */
public static final String GRAPHICS_EQUALIZER = "graphics-equalizer";
public static final String GRAPHICS_EQUALIZER_ISCP = "CEQ";

/**
 * ISCP Command: CEQPOWER
 * POWER ON/OFF
 */
public static final String GRAPHICS_EQUALIZER_POWER = "graphics-equalizer::power";
// public static final String GRAPHICS_EQUALIZER_POWER_ISCP = "CEQPOWER";

/**
 * ISCP Command: CEQPRESET
 * PRESET
 */
public static final String GRAPHICS_EQUALIZER_PRESET = "graphics-equalizer::preset";
// public static final String GRAPHICS_EQUALIZER_PRESET_ISCP = "CEQPRESET";

/**
 * CommandBlock
 * ISCP Command prefix: UHA
 * HD Radio Artist Name Info (Universal Port Dock Only)
 */
public static final String HD_RADIO_ARTIST_NAME_INFO = "hd-radio-artist-name-info";
public static final String HD_RADIO_ARTIST_NAME_INFO_ISCP = "UHA";

/**
 * ISCP Command: UHAnnnnnnnnnn
 * HD Radio Artist Name (variable-length, 64 letters max)
 */
public static final String HD_RADIO_ARTIST_NAME_INFO_NULL = "hd-radio-artist-name-info::null";
// public static final String HD_RADIO_ARTIST_NAME_INFO_NULL_ISCP = "UHAnnnnnnnnnn";

/**
 * ISCP Command: UHAQSTN
 * gets HD Radio Artist Name
 */
public static final String HD_RADIO_ARTIST_NAME_INFO_QUERY = "hd-radio-artist-name-info::query";
// public static final String HD_RADIO_ARTIST_NAME_INFO_QUERY_ISCP = "UHAQSTN";

/**
 * CommandBlock
 * ISCP Command prefix: UHB
 * HD Radio Blend Mode Command (Universal Port Dock Only)
 */
public static final String HD_RADIO_BLEND_MODE = "hd-radio-blend-mode";
public static final String HD_RADIO_BLEND_MODE_ISCP = "UHB";

/**
 * ISCP Command: UHB01
 * sets HD Radio Blend Mode "Analog"
 */
public static final String HD_RADIO_BLEND_MODE_ANALOG = "hd-radio-blend-mode::analog";
// public static final String HD_RADIO_BLEND_MODE_ANALOG_ISCP = "UHB01";

/**
 * ISCP Command: UHB00
 * sets HD Radio Blend Mode "Auto"
 */
public static final String HD_RADIO_BLEND_MODE_AUTO = "hd-radio-blend-mode::auto";
// public static final String HD_RADIO_BLEND_MODE_AUTO_ISCP = "UHB00";

/**
 * ISCP Command: UHBQSTN
 * gets the HD Radio Blend Mode Status
 */
public static final String HD_RADIO_BLEND_MODE_QUERY = "hd-radio-blend-mode::query";
// public static final String HD_RADIO_BLEND_MODE_QUERY_ISCP = "UHBQSTN";

/**
 * CommandBlock
 * ISCP Command prefix: UHC
 * HD Radio Channel Name Info (Universal Port Dock Only)
 */
public static final String HD_RADIO_CHANNEL_NAME_INFO = "hd-radio-channel-name-info";
public static final String HD_RADIO_CHANNEL_NAME_INFO_ISCP = "UHC";

/**
 * ISCP Command: UHCnnnnnnn
 * HD Radio Channel Name (Station Name) (7lettters)
 */
public static final String HD_RADIO_CHANNEL_NAME_INFO_NULL = "hd-radio-channel-name-info::null";
// public static final String HD_RADIO_CHANNEL_NAME_INFO_NULL_ISCP = "UHCnnnnnnn";

/**
 * ISCP Command: UHCQSTN
 * gets HD Radio Channel Name
 */
public static final String HD_RADIO_CHANNEL_NAME_INFO_QUERY = "hd-radio-channel-name-info::query";
// public static final String HD_RADIO_CHANNEL_NAME_INFO_QUERY_ISCP = "UHCQSTN";

/**
 * CommandBlock
 * ISCP Command prefix: UHP
 * HD Radio Channel Program Command (Universal Port Dock Only)
 */
public static final String HD_RADIO_CHANNEL_PROGRAM = "hd-radio-channel-program";
public static final String HD_RADIO_CHANNEL_PROGRAM_ISCP = "UHP";

/**
 * ISCP Command: UHPQSTN
 * gets HD Radio Channel Program
 */
public static final String HD_RADIO_CHANNEL_PROGRAM_QUERY = "hd-radio-channel-program::query";
// public static final String HD_RADIO_CHANNEL_PROGRAM_QUERY_ISCP = "UHPQSTN";

/**
 * CommandBlock
 * ISCP Command prefix: UHD
 * HD Radio Detail Info (Universal Port Dock Only)
 */
public static final String HD_RADIO_DETAIL_INFO = "hd-radio-detail-info";
public static final String HD_RADIO_DETAIL_INFO_ISCP = "UHD";

/**
 * ISCP Command: UHDnnnnnnnnnn
 * HD Radio Title
 */
public static final String HD_RADIO_DETAIL_INFO_NULL = "hd-radio-detail-info::null";
// public static final String HD_RADIO_DETAIL_INFO_NULL_ISCP = "UHDnnnnnnnnnn";

/**
 * ISCP Command: UHDQSTN
 * gets HD Radio Title
 */
public static final String HD_RADIO_DETAIL_INFO_QUERY = "hd-radio-detail-info::query";
// public static final String HD_RADIO_DETAIL_INFO_QUERY_ISCP = "UHDQSTN";

/**
 * CommandBlock
 * ISCP Command prefix: UHT
 * HD Radio Title Info (Universal Port Dock Only)
 */
public static final String HD_RADIO_TITLE_INFO = "hd-radio-title-info";
public static final String HD_RADIO_TITLE_INFO_ISCP = "UHT";

/**
 * ISCP Command: UHTnnnnnnnnnn
 * HD Radio Title (variable-length, 64 letters max)
 */
public static final String HD_RADIO_TITLE_INFO_NULL = "hd-radio-title-info::null";
// public static final String HD_RADIO_TITLE_INFO_NULL_ISCP = "UHTnnnnnnnnnn";

/**
 * ISCP Command: UHTQSTN
 * gets HD Radio Title
 */
public static final String HD_RADIO_TITLE_INFO_QUERY = "hd-radio-title-info::query";
// public static final String HD_RADIO_TITLE_INFO_QUERY_ISCP = "UHTQSTN";

/**
 * CommandBlock
 * ISCP Command prefix: UHS
 * HD Radio Tuner Status (Universal Port Dock Only)
 */
public static final String HD_RADIO_TUNER_STATUS = "hd-radio-tuner-status";
public static final String HD_RADIO_TUNER_STATUS_ISCP = "UHS";

/**
 * ISCP Command: UHSmmnnoo
 * HD Radio Tuner Status (3 bytes)
mm -> "00" not HD, "01" HD
nn -> current Program "01"-"08"
oo -> receivable Program (8 bits are represented in hexadecimal notation. Each bit shows receivable or not.)
 */
public static final String HD_RADIO_TUNER_STATUS_MMNNOO = "hd-radio-tuner-status::mmnnoo";
// public static final String HD_RADIO_TUNER_STATUS_MMNNOO_ISCP = "UHSmmnnoo";

/**
 * ISCP Command: UHSQSTN
 * gets the HD Radio Tuner Status
 */
public static final String HD_RADIO_TUNER_STATUS_QUERY = "hd-radio-tuner-status::query";
// public static final String HD_RADIO_TUNER_STATUS_QUERY_ISCP = "UHSQSTN";

/**
 * CommandBlock
 * ISCP Command prefix: HAO
 * HDMI Audio Out
 */
public static final String HDMI_AUDIO_OUT = "hdmi-audio-out";
public static final String HDMI_AUDIO_OUT_ISCP = "HAO";

/**
 * ISCP Command: HAO02
 * sets Auto
 */
public static final String HDMI_AUDIO_OUT_AUTO = "hdmi-audio-out::auto";
// public static final String HDMI_AUDIO_OUT_AUTO_ISCP = "HAO02";

/**
 * ISCP Command: HAO00
 * sets Off
 */
public static final String HDMI_AUDIO_OUT_OFF = "hdmi-audio-out::off";
// public static final String HDMI_AUDIO_OUT_OFF_ISCP = "HAO00";

/**
 * ISCP Command: HAO01
 * sets On
 */
public static final String HDMI_AUDIO_OUT_ON = "hdmi-audio-out::on";
// public static final String HDMI_AUDIO_OUT_ON_ISCP = "HAO01";

/**
 * ISCP Command: HAOQSTN
 * gets HDMI Audio Out
 */
public static final String HDMI_AUDIO_OUT_QUERY = "hdmi-audio-out::query";
// public static final String HDMI_AUDIO_OUT_QUERY_ISCP = "HAOQSTN";

/**
 * ISCP Command: HAOUP
 * sets HDMI Audio Out Wrap-Around Up
 */
public static final String HDMI_AUDIO_OUT_UP = "hdmi-audio-out::up";
// public static final String HDMI_AUDIO_OUT_UP_ISCP = "HAOUP";

/**
 * CommandBlock
 * ISCP Command prefix: HDO
 * HDMI Output Selector
 */
public static final String HDMI_OUTPUT_SELECTOR = "hdmi-output-selector";
public static final String HDMI_OUTPUT_SELECTOR_ISCP = "HDO";

/**
 * ISCP Command: HDO05
 * sets, Both(Sub)
 */
public static final String HDMI_OUTPUT_SELECTOR_BOTH = "hdmi-output-selector::both";
// public static final String HDMI_OUTPUT_SELECTOR_BOTH_ISCP = "HDO05";

/**
 * ISCP Command: HDO00
 * sets No, Analog
 */
public static final String HDMI_OUTPUT_SELECTOR_NO = "hdmi-output-selector::no";
// public static final String HDMI_OUTPUT_SELECTOR_NO_ISCP = "HDO00";

/**
 * ISCP Command: HDO02
 * sets Out Sub, HDMI Sub
 */
public static final String HDMI_OUTPUT_SELECTOR_OUT_SUB = "hdmi-output-selector::out-sub";
// public static final String HDMI_OUTPUT_SELECTOR_OUT_SUB_ISCP = "HDO02";

/**
 * ISCP Command: HDOQSTN
 * gets The HDMI Out Selector
 */
public static final String HDMI_OUTPUT_SELECTOR_QUERY = "hdmi-output-selector::query";
// public static final String HDMI_OUTPUT_SELECTOR_QUERY_ISCP = "HDOQSTN";

/**
 * ISCP Command: HDOUP
 * sets HDMI Out Selector Wrap-Around Up
 */
public static final String HDMI_OUTPUT_SELECTOR_UP = "hdmi-output-selector::up";
// public static final String HDMI_OUTPUT_SELECTOR_UP_ISCP = "HDOUP";

/**
 * ISCP Command: HDO01
 * sets Yes/Out Main, HDMI Main
 */
public static final String HDMI_OUTPUT_SELECTOR_YES = "hdmi-output-selector::yes";
// public static final String HDMI_OUTPUT_SELECTOR_YES_ISCP = "HDO01";

/**
 * CommandBlock
 * ISCP Command prefix: SLI
 * Input Selector Command
 */
public static final String INPUT_SELECTOR = "input-selector";
public static final String INPUT_SELECTOR_ISCP = "SLI";

/**
 * ISCP Command: SLI07
 * Hidden1
 */
public static final String INPUT_SELECTOR_07 = "input-selector::07";
// public static final String INPUT_SELECTOR_07_ISCP = "SLI07";

/**
 * ISCP Command: SLI25
 * sets AM
 */
public static final String INPUT_SELECTOR_AM = "input-selector::am";
// public static final String INPUT_SELECTOR_AM_ISCP = "SLI25";

/**
 * ISCP Command: SLI23
 * sets CD, TV/CD
 */
public static final String INPUT_SELECTOR_CD = "input-selector::cd";
// public static final String INPUT_SELECTOR_CD_ISCP = "SLI23";

/**
 * ISCP Command: SLIDOWN
 * sets Selector Position Wrap-Around Down
 */
public static final String INPUT_SELECTOR_DOWN = "input-selector::down";
// public static final String INPUT_SELECTOR_DOWN_ISCP = "SLIDOWN";

/**
 * ISCP Command: SLI10
 * sets DVD, BD/DVD
 */
public static final String INPUT_SELECTOR_DVD = "input-selector::dvd";
// public static final String INPUT_SELECTOR_DVD_ISCP = "SLI10";

/**
 * ISCP Command: SLI24
 * sets FM
 */
public static final String INPUT_SELECTOR_FM = "input-selector::fm";
// public static final String INPUT_SELECTOR_FM_ISCP = "SLI24";

/**
 * ISCP Command: SLI28
 * sets INTERNET RADIO, iRadio Favorite
 */
public static final String INPUT_SELECTOR_INTERNET_RADIO = "input-selector::internet-radio";
// public static final String INPUT_SELECTOR_INTERNET_RADIO_ISCP = "SLI28";

/**
 * ISCP Command: SLI30
 * sets MULTI CH
 */
public static final String INPUT_SELECTOR_MULTI_CH = "input-selector::multi-ch";
// public static final String INPUT_SELECTOR_MULTI_CH_ISCP = "SLI30";

/**
 * ISCP Command: SLI27
 * sets MUSIC SERVER, P4S, DLNA
 */
public static final String INPUT_SELECTOR_MUSIC_SERVER = "input-selector::music-server";
// public static final String INPUT_SELECTOR_MUSIC_SERVER_ISCP = "SLI27";

/**
 * ISCP Command: SLI2B
 * sets NETWORK, NET
 */
public static final String INPUT_SELECTOR_NETWORK = "input-selector::network";
// public static final String INPUT_SELECTOR_NETWORK_ISCP = "SLI2B";

/**
 * ISCP Command: SLI22
 * sets PHONO
 */
public static final String INPUT_SELECTOR_PHONO = "input-selector::phono";
// public static final String INPUT_SELECTOR_PHONO_ISCP = "SLI22";

/**
 * ISCP Command: SLIQSTN
 * gets The Selector Position
 */
public static final String INPUT_SELECTOR_QUERY = "input-selector::query";
// public static final String INPUT_SELECTOR_QUERY_ISCP = "SLIQSTN";

/**
 * ISCP Command: SLI32
 * sets SIRIUS
 */
public static final String INPUT_SELECTOR_SIRIUS = "input-selector::sirius";
// public static final String INPUT_SELECTOR_SIRIUS_ISCP = "SLI32";

/**
 * ISCP Command: SLI20
 * sets TAPE(1), TV/TAPE
 */
public static final String INPUT_SELECTOR_TAPE_1 = "input-selector::tape-1";
// public static final String INPUT_SELECTOR_TAPE_1_ISCP = "SLI20";

/**
 * ISCP Command: SLI21
 * sets TAPE2
 */
public static final String INPUT_SELECTOR_TAPE2 = "input-selector::tape2";
// public static final String INPUT_SELECTOR_TAPE2_ISCP = "SLI21";

/**
 * ISCP Command: SLI26
 * sets TUNER
 */
public static final String INPUT_SELECTOR_TUNER = "input-selector::tuner";
// public static final String INPUT_SELECTOR_TUNER_ISCP = "SLI26";

/**
 * ISCP Command: SLI40
 * sets Universal PORT
 */
public static final String INPUT_SELECTOR_UNIVERSAL_PORT = "input-selector::universal-port";
// public static final String INPUT_SELECTOR_UNIVERSAL_PORT_ISCP = "SLI40";

/**
 * ISCP Command: SLIUP
 * sets Selector Position Wrap-Around Up
 */
public static final String INPUT_SELECTOR_UP = "input-selector::up";
// public static final String INPUT_SELECTOR_UP_ISCP = "SLIUP";

/**
 * ISCP Command: SLI2C
 * sets USB(toggle)
 */
public static final String INPUT_SELECTOR_USB = "input-selector::usb";
// public static final String INPUT_SELECTOR_USB_ISCP = "SLI2C";

/**
 * ISCP Command: SLI00
 * sets VIDEO1, VCR/DVR
 */
public static final String INPUT_SELECTOR_VIDEO1 = "input-selector::video1";
// public static final String INPUT_SELECTOR_VIDEO1_ISCP = "SLI00";

/**
 * ISCP Command: SLI01
 * sets VIDEO2, CBL/SAT
 */
public static final String INPUT_SELECTOR_VIDEO2 = "input-selector::video2";
// public static final String INPUT_SELECTOR_VIDEO2_ISCP = "SLI01";

/**
 * ISCP Command: SLI02
 * sets VIDEO3, GAME/TV, GAME
 */
public static final String INPUT_SELECTOR_VIDEO3 = "input-selector::video3";
// public static final String INPUT_SELECTOR_VIDEO3_ISCP = "SLI02";

/**
 * ISCP Command: SLI03
 * sets VIDEO4, AUX1(AUX)
 */
public static final String INPUT_SELECTOR_VIDEO4 = "input-selector::video4";
// public static final String INPUT_SELECTOR_VIDEO4_ISCP = "SLI03";

/**
 * ISCP Command: SLI04
 * sets VIDEO5, AUX2
 */
public static final String INPUT_SELECTOR_VIDEO5 = "input-selector::video5";
// public static final String INPUT_SELECTOR_VIDEO5_ISCP = "SLI04";

/**
 * ISCP Command: SLI05
 * sets VIDEO6, PC
 */
public static final String INPUT_SELECTOR_VIDEO6 = "input-selector::video6";
// public static final String INPUT_SELECTOR_VIDEO6_ISCP = "SLI05";

/**
 * ISCP Command: SLI06
 * sets VIDEO7
 */
public static final String INPUT_SELECTOR_VIDEO7 = "input-selector::video7";
// public static final String INPUT_SELECTOR_VIDEO7_ISCP = "SLI06";

/**
 * ISCP Command: SLI31
 * sets XM
 */
public static final String INPUT_SELECTOR_XM = "input-selector::xm";
// public static final String INPUT_SELECTOR_XM_ISCP = "SLI31";

/**
 * CommandBlock
 * ISCP Command prefix: NPR
 * Internet Radio Preset Command
 */
public static final String INTERNET_RADIO_PRESET = "internet-radio-preset";
public static final String INTERNET_RADIO_PRESET_ISCP = "NPR";

/**
 * ISCP Command: NPRSET
 * preset memory current station
 */
public static final String INTERNET_RADIO_PRESET_SET = "internet-radio-preset::set";
// public static final String INTERNET_RADIO_PRESET_SET_ISCP = "NPRSET";

/**
 * CommandBlock
 * ISCP Command prefix: IAL
 * iPod Album Name Info (Universal Port Dock Only)
 */
public static final String IPOD_ALBUM_NAME_INFO = "ipod-album-name-info";
public static final String IPOD_ALBUM_NAME_INFO_ISCP = "IAL";

/**
 * ISCP Command: IALnnnnnnn
 * iPod Album Name (variable-length, 64 letters max ASCII letter only)
 */
public static final String IPOD_ALBUM_NAME_INFO_NULL = "ipod-album-name-info::null";
// public static final String IPOD_ALBUM_NAME_INFO_NULL_ISCP = "IALnnnnnnn";

/**
 * ISCP Command: IALQSTN
 * gets iPod Album Name
 */
public static final String IPOD_ALBUM_NAME_INFO_QUERY = "ipod-album-name-info::query";
// public static final String IPOD_ALBUM_NAME_INFO_QUERY_ISCP = "IALQSTN";

/**
 * CommandBlock
 * ISCP Command prefix: IAT
 * iPod Artist Name Info (Universal Port Dock Only)
 */
public static final String IPOD_ARTIST_NAME_INFO = "ipod-artist-name-info";
public static final String IPOD_ARTIST_NAME_INFO_ISCP = "IAT";

/**
 * ISCP Command: IATnnnnnnnnnn
 * iPod Artist Name (variable-length, 64 letters max ASCII letter only)
 */
public static final String IPOD_ARTIST_NAME_INFO_NULL = "ipod-artist-name-info::null";
// public static final String IPOD_ARTIST_NAME_INFO_NULL_ISCP = "IATnnnnnnnnnn";

/**
 * ISCP Command: IATQSTN
 * gets iPod Artist Name
 */
public static final String IPOD_ARTIST_NAME_INFO_QUERY = "ipod-artist-name-info::query";
// public static final String IPOD_ARTIST_NAME_INFO_QUERY_ISCP = "IATQSTN";

/**
 * CommandBlock
 * ISCP Command prefix: ILS
 * iPod List Info (Universal Port Dock Extend Mode Only)
 */
public static final String IPOD_LIST_INFO = "ipod-list-info";
public static final String IPOD_LIST_INFO_ISCP = "ILS";

/**
 * ISCP Command: ILStlpnnnnnnnnnn
 * iPod List Info
t ->Information Type (A : ASCII letter, C : Cursor Info)
when t = A,
  l ->Line Info (0-9 : 1st to 10th Line)
  nnnnnnnnn:Listed data (variable-length, 64 letters max ASCII letter only)
  p ->Property (- : no)
when t = C,
  l ->Cursor Position (0-9 : 1st to 10th Line, - : No Cursor)
  p ->Update Type (P : Page Infomation Update ( Page Clear or Disable List Info) , C : Cursor Position Update)
 */
public static final String IPOD_LIST_INFO_NULL = "ipod-list-info::null";
// public static final String IPOD_LIST_INFO_NULL_ISCP = "ILStlpnnnnnnnnnn";

/**
 * CommandBlock
 * ISCP Command prefix: IMD
 * iPod Mode Change (Universal Port Dock Only)
 */
public static final String IPOD_MODE_CHANGE = "ipod-mode-change";
public static final String IPOD_MODE_CHANGE_ISCP = "IMD";

/**
 * ISCP Command: IMDEXT
 * Extend Mode(If available)
 */
public static final String IPOD_MODE_CHANGE_EXT = "ipod-mode-change::ext";
// public static final String IPOD_MODE_CHANGE_EXT_ISCP = "IMDEXT";

/**
 * ISCP Command: IMDQSTN
 * gets iPod Mode Status
 */
public static final String IPOD_MODE_CHANGE_QUERY = "ipod-mode-change::query";
// public static final String IPOD_MODE_CHANGE_QUERY_ISCP = "IMDQSTN";

/**
 * ISCP Command: IMDSTD
 * Standerd Mode
 */
public static final String IPOD_MODE_CHANGE_STD = "ipod-mode-change::std";
// public static final String IPOD_MODE_CHANGE_STD_ISCP = "IMDSTD";

/**
 * ISCP Command: IMDVDC
 * Video Contents in Extended Mode
 */
public static final String IPOD_MODE_CHANGE_VDC = "ipod-mode-change::vdc";
// public static final String IPOD_MODE_CHANGE_VDC_ISCP = "IMDVDC";

/**
 * CommandBlock
 * ISCP Command prefix: IST
 * iPod Play Status (Universal Port Dock Only)
 */
public static final String IPOD_PLAY_STATUS = "ipod-play-status";
public static final String IPOD_PLAY_STATUS_ISCP = "IST";

/**
 * ISCP Command: ISTprs
 * iPod Play Status (3 letters)
p -> Play Status "S" STOP, "P" Play, "p" Pause, "F" FF, "R" FR
r -> Repeat Status "-" no Repeat, "R" All Repeat, "1" Repeat 1,
s -> Shuffle Status "-" no Shuffle, "S" Shuffle, "A" Album Shuffle
 */
public static final String IPOD_PLAY_STATUS_PRS = "ipod-play-status::prs";
// public static final String IPOD_PLAY_STATUS_PRS_ISCP = "ISTprs";

/**
 * ISCP Command: ISTQSTN
 * gets the iPod Play Status
 */
public static final String IPOD_PLAY_STATUS_QUERY = "ipod-play-status::query";
// public static final String IPOD_PLAY_STATUS_QUERY_ISCP = "ISTQSTN";

/**
 * CommandBlock
 * ISCP Command prefix: ITM
 * iPod Time Info (Universal Port Dock Only)
 */
public static final String IPOD_TIME_INFO = "ipod-time-info";
public static final String IPOD_TIME_INFO_ISCP = "ITM";

/**
 * ISCP Command: ITMmm:ss/mm:ss
 * iPod Time Info (Elapsed time/Track Time Max 99:59)
 */
public static final String IPOD_TIME_INFO_MM_SS_MM_SS = "ipod-time-info::mm-ss-mm-ss";
// public static final String IPOD_TIME_INFO_MM_SS_MM_SS_ISCP = "ITMmm:ss/mm:ss";

/**
 * ISCP Command: ITMQSTN
 * gets iPod Time Info
 */
public static final String IPOD_TIME_INFO_QUERY = "ipod-time-info::query";
// public static final String IPOD_TIME_INFO_QUERY_ISCP = "ITMQSTN";

/**
 * CommandBlock
 * ISCP Command prefix: ITI
 * iPod Title Name (Universal Port Dock Only)
 */
public static final String IPOD_TITLE_NAME = "ipod-title-name";
public static final String IPOD_TITLE_NAME_ISCP = "ITI";

/**
 * ISCP Command: ITInnnnnnnnnn
 * iPod Title Name (variable-length, 64 letters max ASCII letter only)
 */
public static final String IPOD_TITLE_NAME_NULL = "ipod-title-name::null";
// public static final String IPOD_TITLE_NAME_NULL_ISCP = "ITInnnnnnnnnn";

/**
 * ISCP Command: ITIQSTN
 * gets iPod Title Name
 */
public static final String IPOD_TITLE_NAME_QUERY = "ipod-title-name::query";
// public static final String IPOD_TITLE_NAME_QUERY_ISCP = "ITIQSTN";

/**
 * CommandBlock
 * ISCP Command prefix: ITR
 * iPod Track Info (Universal Port Dock Only)
 */
public static final String IPOD_TRACK_INFO = "ipod-track-info";
public static final String IPOD_TRACK_INFO_ISCP = "ITR";

/**
 * ISCP Command: ITRcccc/tttt
 * iPod Track Info (Current Track/Toral Track Max 9999)
 */
public static final String IPOD_TRACK_INFO_CCCC_TTTT = "ipod-track-info::cccc-tttt";
// public static final String IPOD_TRACK_INFO_CCCC_TTTT_ISCP = "ITRcccc/tttt";

/**
 * ISCP Command: ITRQSTN
 * gets iPod Time Info
 */
public static final String IPOD_TRACK_INFO_QUERY = "ipod-track-info::query";
// public static final String IPOD_TRACK_INFO_QUERY_ISCP = "ITRQSTN";

/**
 * CommandBlock
 * ISCP Command prefix: ISF
 * ISF Mode
 */
public static final String ISF_MODE = "isf-mode";
public static final String ISF_MODE_ISCP = "ISF";

/**
 * ISCP Command: ISF00
 * sets ISF Mode Custom
 */
public static final String ISF_MODE_CUSTOM = "isf-mode::custom";
// public static final String ISF_MODE_CUSTOM_ISCP = "ISF00";

/**
 * ISCP Command: ISF01
 * sets ISF Mode Day
 */
public static final String ISF_MODE_DAY = "isf-mode::day";
// public static final String ISF_MODE_DAY_ISCP = "ISF01";

/**
 * ISCP Command: ISF02
 * sets ISF Mode Night
 */
public static final String ISF_MODE_NIGHT = "isf-mode::night";
// public static final String ISF_MODE_NIGHT_ISCP = "ISF02";

/**
 * ISCP Command: ISFQSTN
 * gets The ISF Mode State
 */
public static final String ISF_MODE_QUERY = "isf-mode::query";
// public static final String ISF_MODE_QUERY_ISCP = "ISFQSTN";

/**
 * ISCP Command: ISFUP
 * sets ISF Mode State Wrap-Around Up
 */
public static final String ISF_MODE_UP = "isf-mode::up";
// public static final String ISF_MODE_UP_ISCP = "ISFUP";

/**
 * CommandBlock
 * ISCP Command prefix: LTN
 * Late Night Command
 */
public static final String LATE_NIGHT = "late-night";
public static final String LATE_NIGHT_ISCP = "LTN";

/**
 * ISCP Command: LTN03
 * sets Late Night Auto@Dolby TrueHD
 */
public static final String LATE_NIGHT_AUTO_DOLBY_TRUEHD = "late-night::auto-dolby-truehd";
// public static final String LATE_NIGHT_AUTO_DOLBY_TRUEHD_ISCP = "LTN03";

/**
 * ISCP Command: LTN02
 * sets Late Night High@DolbyDigital,(On@Dolby TrueHD)
 */
public static final String LATE_NIGHT_HIGH_DOLBYDIGITAL = "late-night::high-dolbydigital";
// public static final String LATE_NIGHT_HIGH_DOLBYDIGITAL_ISCP = "LTN02";

/**
 * ISCP Command: LTN01
 * sets Late Night Low@DolbyDigital,On@Dolby TrueHD
 */
public static final String LATE_NIGHT_LOW_DOLBYDIGITAL = "late-night::low-dolbydigital";
// public static final String LATE_NIGHT_LOW_DOLBYDIGITAL_ISCP = "LTN01";

/**
 * ISCP Command: LTN00
 * sets Late Night Off
 */
public static final String LATE_NIGHT_OFF = "late-night::off";
// public static final String LATE_NIGHT_OFF_ISCP = "LTN00";

/**
 * ISCP Command: LTNQSTN
 * gets The Late Night Level
 */
public static final String LATE_NIGHT_QUERY = "late-night::query";
// public static final String LATE_NIGHT_QUERY_ISCP = "LTNQSTN";

/**
 * ISCP Command: LTNUP
 * sets Late Night State Wrap-Around Up
 */
public static final String LATE_NIGHT_UP = "late-night::up";
// public static final String LATE_NIGHT_UP_ISCP = "LTNUP";

/**
 * CommandBlock
 * ISCP Command prefix: LMD
 * Listening Mode Command
 */
public static final String LISTENING_MODE = "listening-mode";
public static final String LISTENING_MODE_ISCP = "LMD";

/**
 * ISCP Command: LMD05
 * sets ACTION, Game-Action
 */
public static final String LISTENING_MODE_ACTION = "listening-mode::action";
// public static final String LISTENING_MODE_ACTION_ISCP = "LMD05";

/**
 * ISCP Command: LMD0C
 * sets ALL CH STEREO
 */
public static final String LISTENING_MODE_ALL_CH_STEREO = "listening-mode::all-ch-stereo";
// public static final String LISTENING_MODE_ALL_CH_STEREO_ISCP = "LMD0C";

/**
 * ISCP Command: LMD16
 * sets Audyssey DSX
 */
public static final String LISTENING_MODE_AUDYSSEY_DSX = "listening-mode::audyssey-dsx";
// public static final String LISTENING_MODE_AUDYSSEY_DSX_ISCP = "LMD16";

/**
 * ISCP Command: LMD01
 * sets DIRECT
 */
public static final String LISTENING_MODE_DIRECT = "listening-mode::direct";
// public static final String LISTENING_MODE_DIRECT_ISCP = "LMD01";

/**
 * ISCP Command: LMD41
 * sets Dolby EX
 */
public static final String LISTENING_MODE_DOLBY_EX = "listening-mode::dolby-ex";
// public static final String LISTENING_MODE_DOLBY_EX_ISCP = "LMD41";

/**
 * ISCP Command: LMDA7
 * sets Dolby EX + Audyssey DSX
 */
public static final String LISTENING_MODE_DOLBY_EX_AUDYSSEY_DSX = "listening-mode::dolby-ex-audyssey-dsx";
// public static final String LISTENING_MODE_DOLBY_EX_AUDYSSEY_DSX_ISCP = "LMDA7";

/**
 * ISCP Command: LMD14
 * sets DOLBY VIRTUAL
 */
public static final String LISTENING_MODE_DOLBY_VIRTUAL = "listening-mode::dolby-virtual";
// public static final String LISTENING_MODE_DOLBY_VIRTUAL_ISCP = "LMD14";

/**
 * ISCP Command: LMDDOWN
 * sets Listening Mode Wrap-Around Down
 */
public static final String LISTENING_MODE_DOWN = "listening-mode::down";
// public static final String LISTENING_MODE_DOWN_ISCP = "LMDDOWN";

/**
 * ISCP Command: LMD15
 * sets DTS Surround Sensation
 */
public static final String LISTENING_MODE_DTS_SURROUND_SENSATION = "listening-mode::dts-surround-sensation";
// public static final String LISTENING_MODE_DTS_SURROUND_SENSATION_ISCP = "LMD15";

/**
 * ISCP Command: LMD0E
 * sets ENHANCED 7/ENHANCE, Game-Sports
 */
public static final String LISTENING_MODE_ENHANCED_7 = "listening-mode::enhanced-7";
// public static final String LISTENING_MODE_ENHANCED_7_ISCP = "LMD0E";

/**
 * ISCP Command: LMD03
 * sets FILM, Game-RPG
 */
public static final String LISTENING_MODE_FILM = "listening-mode::film";
// public static final String LISTENING_MODE_FILM_ISCP = "LMD03";

/**
 * ISCP Command: LMD13
 * sets FULL MONO
 */
public static final String LISTENING_MODE_FULL_MONO = "listening-mode::full-mono";
// public static final String LISTENING_MODE_FULL_MONO_ISCP = "LMD13";

/**
 * ISCP Command: LMDGAME
 * sets Listening Mode Wrap-Around Up
 */
public static final String LISTENING_MODE_GAME = "listening-mode::game";
// public static final String LISTENING_MODE_GAME_ISCP = "LMDGAME";

/**
 * ISCP Command: LMD0F
 * sets MONO
 */
public static final String LISTENING_MODE_MONO = "listening-mode::mono";
// public static final String LISTENING_MODE_MONO_ISCP = "LMD0F";

/**
 * ISCP Command: LMD07
 * sets MONO MOVIE
 */
public static final String LISTENING_MODE_MONO_MOVIE = "listening-mode::mono-movie";
// public static final String LISTENING_MODE_MONO_MOVIE_ISCP = "LMD07";

/**
 * ISCP Command: LMDMOVIE
 * sets Listening Mode Wrap-Around Up
 */
public static final String LISTENING_MODE_MOVIE = "listening-mode::movie";
// public static final String LISTENING_MODE_MOVIE_ISCP = "LMDMOVIE";

/**
 * ISCP Command: LMD12
 * sets MULTIPLEX
 */
public static final String LISTENING_MODE_MULTIPLEX = "listening-mode::multiplex";
// public static final String LISTENING_MODE_MULTIPLEX_ISCP = "LMD12";

/**
 * ISCP Command: LMDMUSIC
 * sets Listening Mode Wrap-Around Up
 */
public static final String LISTENING_MODE_MUSIC = "listening-mode::music";
// public static final String LISTENING_MODE_MUSIC_ISCP = "LMDMUSIC";

/**
 * ISCP Command: LMD06
 * sets MUSICAL, Game-Rock
 */
public static final String LISTENING_MODE_MUSICAL = "listening-mode::musical";
// public static final String LISTENING_MODE_MUSICAL_ISCP = "LMD06";

/**
 * ISCP Command: LMD8C
 * sets Neo:6/Neo:X THX Music
 */
public static final String LISTENING_MODE_NEO_6 = "listening-mode::neo-6";
// public static final String LISTENING_MODE_NEO_6_ISCP = "LMD8C";

/**
 * ISCP Command: LMD82
 * sets Neo:6 Cinema/Neo:X Cinema
 */
public static final String LISTENING_MODE_NEO_6_CINEMA = "listening-mode::neo-6-cinema";
// public static final String LISTENING_MODE_NEO_6_CINEMA_ISCP = "LMD82";

/**
 * ISCP Command: LMDA3
 * sets Neo:6 Cinema + Audyssey DSX
 */
public static final String LISTENING_MODE_NEO_6_CINEMA_AUDYSSEY_DSX = "listening-mode::neo-6-cinema-audyssey-dsx";
// public static final String LISTENING_MODE_NEO_6_CINEMA_AUDYSSEY_DSX_ISCP = "LMDA3";

/**
 * ISCP Command: LMD91
 * sets Neo:6 Cinema DTS Surround Sensation
 */
public static final String LISTENING_MODE_NEO_6_CINEMA_DTS_SURROUND_SENSATION = "listening-mode::neo-6-cinema-dts-surround-sensation";
// public static final String LISTENING_MODE_NEO_6_CINEMA_DTS_SURROUND_SENSATION_ISCP = "LMD91";

/**
 * ISCP Command: LMD83
 * sets Neo:6 Music/Neo:X Music
 */
public static final String LISTENING_MODE_NEO_6_MUSIC = "listening-mode::neo-6-music";
// public static final String LISTENING_MODE_NEO_6_MUSIC_ISCP = "LMD83";

/**
 * ISCP Command: LMDA4
 * sets Neo:6 Music + Audyssey DSX
 */
public static final String LISTENING_MODE_NEO_6_MUSIC_AUDYSSEY_DSX = "listening-mode::neo-6-music-audyssey-dsx";
// public static final String LISTENING_MODE_NEO_6_MUSIC_AUDYSSEY_DSX_ISCP = "LMDA4";

/**
 * ISCP Command: LMD92
 * sets Neo:6 Music DTS Surround Sensation
 */
public static final String LISTENING_MODE_NEO_6_MUSIC_DTS_SURROUND_SENSATION = "listening-mode::neo-6-music-dts-surround-sensation";
// public static final String LISTENING_MODE_NEO_6_MUSIC_DTS_SURROUND_SENSATION_ISCP = "LMD92";

/**
 * ISCP Command: LMD9A
 * sets Neo:X Game
 */
public static final String LISTENING_MODE_NEO_X_GAME = "listening-mode::neo-x-game";
// public static final String LISTENING_MODE_NEO_X_GAME_ISCP = "LMD9A";

/**
 * ISCP Command: LMD93
 * sets Neural Digital Music
 */
public static final String LISTENING_MODE_NEURAL_DIGITAL_MUSIC = "listening-mode::neural-digital-music";
// public static final String LISTENING_MODE_NEURAL_DIGITAL_MUSIC_ISCP = "LMD93";

/**
 * ISCP Command: LMDA6
 * sets Neural Digital Music + Audyssey DSX
 */
public static final String LISTENING_MODE_NEURAL_DIGITAL_MUSIC_AUDYSSEY_DSX = "listening-mode::neural-digital-music-audyssey-dsx";
// public static final String LISTENING_MODE_NEURAL_DIGITAL_MUSIC_AUDYSSEY_DSX_ISCP = "LMDA6";

/**
 * ISCP Command: LMD87
 * sets Neural Surr
 */
public static final String LISTENING_MODE_NEURAL_SURR = "listening-mode::neural-surr";
// public static final String LISTENING_MODE_NEURAL_SURR_ISCP = "LMD87";

/**
 * ISCP Command: LMDA5
 * sets Neural Surround + Audyssey DSX
 */
public static final String LISTENING_MODE_NEURAL_SURROUND_AUDYSSEY_DSX = "listening-mode::neural-surround-audyssey-dsx";
// public static final String LISTENING_MODE_NEURAL_SURROUND_AUDYSSEY_DSX_ISCP = "LMDA5";

/**
 * ISCP Command: LMD88
 * sets Neural THX/Neural Surround
 */
public static final String LISTENING_MODE_NEURAL_THX = "listening-mode::neural-thx";
// public static final String LISTENING_MODE_NEURAL_THX_ISCP = "LMD88";

/**
 * ISCP Command: LMD8D
 * sets Neural THX Cinema
 */
public static final String LISTENING_MODE_NEURAL_THX_CINEMA = "listening-mode::neural-thx-cinema";
// public static final String LISTENING_MODE_NEURAL_THX_CINEMA_ISCP = "LMD8D";

/**
 * ISCP Command: LMD8F
 * sets Neural THX Games
 */
public static final String LISTENING_MODE_NEURAL_THX_GAMES = "listening-mode::neural-thx-games";
// public static final String LISTENING_MODE_NEURAL_THX_GAMES_ISCP = "LMD8F";

/**
 * ISCP Command: LMD8E
 * sets Neural THX Music
 */
public static final String LISTENING_MODE_NEURAL_THX_MUSIC = "listening-mode::neural-thx-music";
// public static final String LISTENING_MODE_NEURAL_THX_MUSIC_ISCP = "LMD8E";

/**
 * ISCP Command: LMD8B
 * sets PLII/PLIIx THX Music
 */
public static final String LISTENING_MODE_PLII = "listening-mode::plii";
// public static final String LISTENING_MODE_PLII_ISCP = "LMD8B";

/**
 * ISCP Command: LMDA2
 * sets PLIIx/PLII Game + Audyssey DSX
 */
public static final String LISTENING_MODE_PLIIX = "listening-mode::pliix";
// public static final String LISTENING_MODE_PLIIX_ISCP = "LMDA2";

/**
 * ISCP Command: LMD90
 * sets PLIIz Height
 */
public static final String LISTENING_MODE_PLIIZ_HEIGHT = "listening-mode::pliiz-height";
// public static final String LISTENING_MODE_PLIIZ_HEIGHT_ISCP = "LMD90";

/**
 * ISCP Command: LMD94
 * sets PLIIz Height + THX Cinema
 */
public static final String LISTENING_MODE_PLIIZ_HEIGHT_THX_CINEMA = "listening-mode::pliiz-height-thx-cinema";
// public static final String LISTENING_MODE_PLIIZ_HEIGHT_THX_CINEMA_ISCP = "LMD94";

/**
 * ISCP Command: LMD96
 * sets PLIIz Height + THX Games
 */
public static final String LISTENING_MODE_PLIIZ_HEIGHT_THX_GAMES = "listening-mode::pliiz-height-thx-games";
// public static final String LISTENING_MODE_PLIIZ_HEIGHT_THX_GAMES_ISCP = "LMD96";

/**
 * ISCP Command: LMD95
 * sets PLIIz Height + THX Music
 */
public static final String LISTENING_MODE_PLIIZ_HEIGHT_THX_MUSIC = "listening-mode::pliiz-height-thx-music";
// public static final String LISTENING_MODE_PLIIZ_HEIGHT_THX_MUSIC_ISCP = "LMD95";

/**
 * ISCP Command: LMD99
 * sets PLIIz Height + THX U2/S2 Games
 */
public static final String LISTENING_MODE_PLIIZ_HEIGHT_THX_U2 = "listening-mode::pliiz-height-thx-u2";
// public static final String LISTENING_MODE_PLIIZ_HEIGHT_THX_U2_ISCP = "LMD99";

/**
 * ISCP Command: LMD11
 * sets PURE AUDIO
 */
public static final String LISTENING_MODE_PURE_AUDIO = "listening-mode::pure-audio";
// public static final String LISTENING_MODE_PURE_AUDIO_ISCP = "LMD11";

/**
 * ISCP Command: LMDQSTN
 * gets The Listening Mode
 */
public static final String LISTENING_MODE_QUERY = "listening-mode::query";
// public static final String LISTENING_MODE_QUERY_ISCP = "LMDQSTN";

/**
 * ISCP Command: LMD00
 * sets STEREO
 */
public static final String LISTENING_MODE_STEREO = "listening-mode::stereo";
// public static final String LISTENING_MODE_STEREO_ISCP = "LMD00";

/**
 * ISCP Command: LMD40
 * sets Straight Decode
 */
public static final String LISTENING_MODE_STRAIGHT_DECODE = "listening-mode::straight-decode";
// public static final String LISTENING_MODE_STRAIGHT_DECODE_ISCP = "LMD40";

/**
 * ISCP Command: LMD0A
 * sets STUDIO-MIX
 */
public static final String LISTENING_MODE_STUDIO_MIX = "listening-mode::studio-mix";
// public static final String LISTENING_MODE_STUDIO_MIX_ISCP = "LMD0A";

/**
 * ISCP Command: LMD02
 * sets SURROUND
 */
public static final String LISTENING_MODE_SURROUND = "listening-mode::surround";
// public static final String LISTENING_MODE_SURROUND_ISCP = "LMD02";

/**
 * ISCP Command: LMD0D
 * sets THEATER-DIMENSIONAL
 */
public static final String LISTENING_MODE_THEATER_DIMENSIONAL = "listening-mode::theater-dimensional";
// public static final String LISTENING_MODE_THEATER_DIMENSIONAL_ISCP = "LMD0D";

/**
 * ISCP Command: LMD04
 * sets THX
 */
public static final String LISTENING_MODE_THX = "listening-mode::thx";
// public static final String LISTENING_MODE_THX_ISCP = "LMD04";

/**
 * ISCP Command: LMD42
 * sets THX Cinema
 */
public static final String LISTENING_MODE_THX_CINEMA = "listening-mode::thx-cinema";
// public static final String LISTENING_MODE_THX_CINEMA_ISCP = "LMD42";

/**
 * ISCP Command: LMD52
 * sets THX Games Mode,THX U2/S2/I/S Games
 */
public static final String LISTENING_MODE_THX_GAMES = "listening-mode::thx-games";
// public static final String LISTENING_MODE_THX_GAMES_ISCP = "LMD52";

/**
 * ISCP Command: LMD44
 * sets THX Music
 */
public static final String LISTENING_MODE_THX_MUSIC = "listening-mode::thx-music";
// public static final String LISTENING_MODE_THX_MUSIC_ISCP = "LMD44";

/**
 * ISCP Command: LMD51
 * sets THX MusicMode,THX U2/S2/I/S Music
 */
public static final String LISTENING_MODE_THX_MUSICMODE = "listening-mode::thx-musicmode";
// public static final String LISTENING_MODE_THX_MUSICMODE_ISCP = "LMD51";

/**
 * ISCP Command: LMD43
 * sets THX Surround EX
 */
public static final String LISTENING_MODE_THX_SURROUND_EX = "listening-mode::thx-surround-ex";
// public static final String LISTENING_MODE_THX_SURROUND_EX_ISCP = "LMD43";

/**
 * ISCP Command: LMD50
 * sets THX U2/S2/I/S Cinema/Cinema2
 */
public static final String LISTENING_MODE_THX_U2 = "listening-mode::thx-u2";
// public static final String LISTENING_MODE_THX_U2_ISCP = "LMD50";

/**
 * ISCP Command: LMD0B
 * sets TV LOGIC
 */
public static final String LISTENING_MODE_TV_LOGIC = "listening-mode::tv-logic";
// public static final String LISTENING_MODE_TV_LOGIC_ISCP = "LMD0B";

/**
 * ISCP Command: LMDUP
 * sets Listening Mode Wrap-Around Up
 */
public static final String LISTENING_MODE_UP = "listening-mode::up";
// public static final String LISTENING_MODE_UP_ISCP = "LMDUP";

/**
 * ISCP Command: LMD1F
 * sets Whole House Mode
 */
public static final String LISTENING_MODE_WHOLE_HOUSE = "listening-mode::whole-house";
// public static final String LISTENING_MODE_WHOLE_HOUSE_ISCP = "LMD1F";

/**
 * CommandBlock
 * ISCP Command prefix: MVL
 * Master Volume Command
 */
public static final String MASTER_VOLUME = "master-volume";
public static final String MASTER_VOLUME_ISCP = "MVL";

/**
 * ISCP Command: MVLDOWN
 * sets Volume Level Down
 */
public static final String MASTER_VOLUME_LEVEL_DOWN = "master-volume::level-down";
// public static final String MASTER_VOLUME_LEVEL_DOWN_ISCP = "MVLDOWN";

/**
 * ISCP Command: MVLDOWN1
 * sets Volume Level Down 1dB Step
 */
public static final String MASTER_VOLUME_LEVEL_DOWN_1DB_STEP = "master-volume::level-down-1db-step";
// public static final String MASTER_VOLUME_LEVEL_DOWN_1DB_STEP_ISCP = "MVLDOWN1";

/**
 * ISCP Command: MVLUP
 * sets Volume Level Up
 */
public static final String MASTER_VOLUME_LEVEL_UP = "master-volume::level-up";
// public static final String MASTER_VOLUME_LEVEL_UP_ISCP = "MVLUP";

/**
 * ISCP Command: MVLUP1
 * sets Volume Level Up 1dB Step
 */
public static final String MASTER_VOLUME_LEVEL_UP_1DB_STEP = "master-volume::level-up-1db-step";
// public static final String MASTER_VOLUME_LEVEL_UP_1DB_STEP_ISCP = "MVLUP1";

/**
 * ISCP Command: MVLQSTN
 * gets the Volume Level
 */
public static final String MASTER_VOLUME_QUERY = "master-volume::query";
// public static final String MASTER_VOLUME_QUERY_ISCP = "MVLQSTN";

/**
 * CommandBlock
 * ISCP Command prefix: CMD
 * MD Recorder Operation Command
 */
public static final String MD_RECORDER = "md-recorder";
public static final String MD_RECORDER_ISCP = "CMD";

/**
 * ISCP Command: CMD1
 * 1.0
 */
public static final String MD_RECORDER_1 = "md-recorder::1";
// public static final String MD_RECORDER_1_ISCP = "CMD1";

/**
 * ISCP Command: CMD10/0
 * 10/0
 */
public static final String MD_RECORDER_10_0 = "md-recorder::10-0";
// public static final String MD_RECORDER_10_0_ISCP = "CMD10/0";

/**
 * ISCP Command: CMD2
 * 2.0
 */
public static final String MD_RECORDER_2 = "md-recorder::2";
// public static final String MD_RECORDER_2_ISCP = "CMD2";

/**
 * ISCP Command: CMD3
 * 3.0
 */
public static final String MD_RECORDER_3 = "md-recorder::3";
// public static final String MD_RECORDER_3_ISCP = "CMD3";

/**
 * ISCP Command: CMD4
 * 4.0
 */
public static final String MD_RECORDER_4 = "md-recorder::4";
// public static final String MD_RECORDER_4_ISCP = "CMD4";

/**
 * ISCP Command: CMD5
 * 5.0
 */
public static final String MD_RECORDER_5 = "md-recorder::5";
// public static final String MD_RECORDER_5_ISCP = "CMD5";

/**
 * ISCP Command: CMD6
 * 6.0
 */
public static final String MD_RECORDER_6 = "md-recorder::6";
// public static final String MD_RECORDER_6_ISCP = "CMD6";

/**
 * ISCP Command: CMD7
 * 7.0
 */
public static final String MD_RECORDER_7 = "md-recorder::7";
// public static final String MD_RECORDER_7_ISCP = "CMD7";

/**
 * ISCP Command: CMD8
 * 8.0
 */
public static final String MD_RECORDER_8 = "md-recorder::8";
// public static final String MD_RECORDER_8_ISCP = "CMD8";

/**
 * ISCP Command: CMD9
 * 9.0
 */
public static final String MD_RECORDER_9 = "md-recorder::9";
// public static final String MD_RECORDER_9_ISCP = "CMD9";

/**
 * ISCP Command: CMDCLEAR
 * CLEAR
 */
public static final String MD_RECORDER_CLEAR = "md-recorder::clear";
// public static final String MD_RECORDER_CLEAR_ISCP = "CMDCLEAR";

/**
 * ISCP Command: CMDDISP
 * DISPLAY
 */
public static final String MD_RECORDER_DISP = "md-recorder::disp";
// public static final String MD_RECORDER_DISP_ISCP = "CMDDISP";

/**
 * ISCP Command: CMDEJECT
 * EJECT
 */
public static final String MD_RECORDER_EJECT = "md-recorder::eject";
// public static final String MD_RECORDER_EJECT_ISCP = "CMDEJECT";

/**
 * ISCP Command: CMDENTER
 * ENTER
 */
public static final String MD_RECORDER_ENTER = "md-recorder::enter";
// public static final String MD_RECORDER_ENTER_ISCP = "CMDENTER";

/**
 * ISCP Command: CMDFF
 * FF >>
 */
public static final String MD_RECORDER_FF = "md-recorder::ff";
// public static final String MD_RECORDER_FF_ISCP = "CMDFF";

/**
 * ISCP Command: CMDGROUP
 * GROUP
 */
public static final String MD_RECORDER_GROUP = "md-recorder::group";
// public static final String MD_RECORDER_GROUP_ISCP = "CMDGROUP";

/**
 * ISCP Command: CMDM.SCAN
 * MUSIC SCAN
 */
public static final String MD_RECORDER_M_SCAN = "md-recorder::m-scan";
// public static final String MD_RECORDER_M_SCAN_ISCP = "CMDM.SCAN";

/**
 * ISCP Command: CMDMEMORY
 * MEMORY
 */
public static final String MD_RECORDER_MEMORY = "md-recorder::memory";
// public static final String MD_RECORDER_MEMORY_ISCP = "CMDMEMORY";

/**
 * ISCP Command: CMDNAME
 * NAME
 */
public static final String MD_RECORDER_NAME = "md-recorder::name";
// public static final String MD_RECORDER_NAME_ISCP = "CMDNAME";

/**
 * ISCP Command: CMDnn/nnn
 * --/---
 */
public static final String MD_RECORDER_NULL = "md-recorder::null";
// public static final String MD_RECORDER_NULL_ISCP = "CMDnn/nnn";

/**
 * ISCP Command: CMDP.MODE
 * PLAY MODE
 */
public static final String MD_RECORDER_P_MODE = "md-recorder::p-mode";
// public static final String MD_RECORDER_P_MODE_ISCP = "CMDP.MODE";

/**
 * ISCP Command: CMDPAUSE
 * PAUSE
 */
public static final String MD_RECORDER_PAUSE = "md-recorder::pause";
// public static final String MD_RECORDER_PAUSE_ISCP = "CMDPAUSE";

/**
 * ISCP Command: CMDPLAY
 * PLAY
 */
public static final String MD_RECORDER_PLAY = "md-recorder::play";
// public static final String MD_RECORDER_PLAY_ISCP = "CMDPLAY";

/**
 * ISCP Command: CMDPOWER
 * POWER ON/OFF
 */
public static final String MD_RECORDER_POWER = "md-recorder::power";
// public static final String MD_RECORDER_POWER_ISCP = "CMDPOWER";

/**
 * ISCP Command: CMDRANDOM
 * RANDOM
 */
public static final String MD_RECORDER_RANDOM = "md-recorder::random";
// public static final String MD_RECORDER_RANDOM_ISCP = "CMDRANDOM";

/**
 * ISCP Command: CMDREC
 * REC
 */
public static final String MD_RECORDER_REC = "md-recorder::rec";
// public static final String MD_RECORDER_REC_ISCP = "CMDREC";

/**
 * ISCP Command: CMDREPEAT
 * REPEAT
 */
public static final String MD_RECORDER_REPEAT = "md-recorder::repeat";
// public static final String MD_RECORDER_REPEAT_ISCP = "CMDREPEAT";

/**
 * ISCP Command: CMDREW
 * REW <<
 */
public static final String MD_RECORDER_REW = "md-recorder::rew";
// public static final String MD_RECORDER_REW_ISCP = "CMDREW";

/**
 * ISCP Command: CMDSCROLL
 * SCROLL
 */
public static final String MD_RECORDER_SCROLL = "md-recorder::scroll";
// public static final String MD_RECORDER_SCROLL_ISCP = "CMDSCROLL";

/**
 * ISCP Command: CMDSKIP.F
 * >>I
 */
public static final String MD_RECORDER_SKIP_F = "md-recorder::skip-f";
// public static final String MD_RECORDER_SKIP_F_ISCP = "CMDSKIP.F";

/**
 * ISCP Command: CMDSKIP.R
 * I<<
 */
public static final String MD_RECORDER_SKIP_R = "md-recorder::skip-r";
// public static final String MD_RECORDER_SKIP_R_ISCP = "CMDSKIP.R";

/**
 * ISCP Command: CMDSTBY
 * STANDBY
 */
public static final String MD_RECORDER_STBY = "md-recorder::stby";
// public static final String MD_RECORDER_STBY_ISCP = "CMDSTBY";

/**
 * ISCP Command: CMDSTOP
 * STOP
 */
public static final String MD_RECORDER_STOP = "md-recorder::stop";
// public static final String MD_RECORDER_STOP_ISCP = "CMDSTOP";

/**
 * CommandBlock
 * ISCP Command prefix: MEM
 * Memory Setup Command
 */
public static final String MEMORY_SETUP = "memory-setup";
public static final String MEMORY_SETUP_ISCP = "MEM";

/**
 * ISCP Command: MEMLOCK
 * locks memory
 */
public static final String MEMORY_SETUP_LOCK = "memory-setup::lock";
// public static final String MEMORY_SETUP_LOCK_ISCP = "MEMLOCK";

/**
 * ISCP Command: MEMRCL
 * recalls memory
 */
public static final String MEMORY_SETUP_RCL = "memory-setup::rcl";
// public static final String MEMORY_SETUP_RCL_ISCP = "MEMRCL";

/**
 * ISCP Command: MEMSTR
 * stores memory
 */
public static final String MEMORY_SETUP_STR = "memory-setup::str";
// public static final String MEMORY_SETUP_STR_ISCP = "MEMSTR";

/**
 * ISCP Command: MEMUNLK
 * unlocks memory
 */
public static final String MEMORY_SETUP_UNLK = "memory-setup::unlk";
// public static final String MEMORY_SETUP_UNLK_ISCP = "MEMUNLK";

/**
 * CommandBlock
 * ISCP Command prefix: RES
 * Monitor Out Resolution
 */
public static final String MONITOR_OUT_RESOLUTION = "monitor-out-resolution";
public static final String MONITOR_OUT_RESOLUTION_ISCP = "RES";

/**
 * ISCP Command: RES04
 * sets 1080i
 */
public static final String MONITOR_OUT_RESOLUTION_1080I = "monitor-out-resolution::1080i";
// public static final String MONITOR_OUT_RESOLUTION_1080I_ISCP = "RES04";

/**
 * ISCP Command: RES07
 * sets 1080p/24fs(HDMI Output Only)
 */
public static final String MONITOR_OUT_RESOLUTION_1080P = "monitor-out-resolution::1080p";
// public static final String MONITOR_OUT_RESOLUTION_1080P_ISCP = "RES07";

/**
 * ISCP Command: RES02
 * sets 480p
 */
public static final String MONITOR_OUT_RESOLUTION_480P = "monitor-out-resolution::480p";
// public static final String MONITOR_OUT_RESOLUTION_480P_ISCP = "RES02";

/**
 * ISCP Command: RES03
 * sets 720p
 */
public static final String MONITOR_OUT_RESOLUTION_720P = "monitor-out-resolution::720p";
// public static final String MONITOR_OUT_RESOLUTION_720P_ISCP = "RES03";

/**
 * ISCP Command: RES01
 * sets Auto(HDMI Output Only)
 */
public static final String MONITOR_OUT_RESOLUTION_AUTO = "monitor-out-resolution::auto";
// public static final String MONITOR_OUT_RESOLUTION_AUTO_ISCP = "RES01";

/**
 * ISCP Command: RESQSTN
 * gets The Monitor Out Resolution
 */
public static final String MONITOR_OUT_RESOLUTION_QUERY = "monitor-out-resolution::query";
// public static final String MONITOR_OUT_RESOLUTION_QUERY_ISCP = "RESQSTN";

/**
 * ISCP Command: RES06
 * sets Source
 */
public static final String MONITOR_OUT_RESOLUTION_SOURCE = "monitor-out-resolution::source";
// public static final String MONITOR_OUT_RESOLUTION_SOURCE_ISCP = "RES06";

/**
 * ISCP Command: RES00
 * sets Through
 */
public static final String MONITOR_OUT_RESOLUTION_THROUGH = "monitor-out-resolution::through";
// public static final String MONITOR_OUT_RESOLUTION_THROUGH_ISCP = "RES00";

/**
 * ISCP Command: RESUP
 * sets Monitor Out Resolution Wrap-Around Up
 */
public static final String MONITOR_OUT_RESOLUTION_UP = "monitor-out-resolution::up";
// public static final String MONITOR_OUT_RESOLUTION_UP_ISCP = "RESUP";

/**
 * CommandBlock
 * ISCP Command prefix: MOT
 * Music Optimizer
 */
public static final String MUSIC_OPTIMIZER = "music-optimizer";
public static final String MUSIC_OPTIMIZER_ISCP = "MOT";

/**
 * ISCP Command: MOT00
 * sets Music Optimizer Off
 */
public static final String MUSIC_OPTIMIZER_OFF = "music-optimizer::off";
// public static final String MUSIC_OPTIMIZER_OFF_ISCP = "MOT00";

/**
 * ISCP Command: MOT01
 * sets Music Optimizer On
 */
public static final String MUSIC_OPTIMIZER_ON = "music-optimizer::on";
// public static final String MUSIC_OPTIMIZER_ON_ISCP = "MOT01";

/**
 * ISCP Command: MOTQSTN
 * gets The Dolby Volume State
 */
public static final String MUSIC_OPTIMIZER_QUERY = "music-optimizer::query";
// public static final String MUSIC_OPTIMIZER_QUERY_ISCP = "MOTQSTN";

/**
 * ISCP Command: MOTUP
 * sets Music Optimizer State Wrap-Around Up
 */
public static final String MUSIC_OPTIMIZER_UP = "music-optimizer::up";
// public static final String MUSIC_OPTIMIZER_UP_ISCP = "MOTUP";

/**
 * CommandBlock
 * ISCP Command prefix: NKY
 * NET Keyboard(for Network Control Only)
 */
public static final String NET_KEYBOARD = "net-keyboard";
public static final String NET_KEYBOARD_ISCP = "NKY";

/**
 * ISCP Command: NKYll
 * waiting Keyboard Input
ll -> category
 00: Off ( Exit Keyboard Input )
 01: User Name
 02: Password
 03: Artist Name
 04: Album Name
 05: Song Name
 06: Station Name
 07: Tag Name
 08: Artist or Song
 09: Episode Name
 0A: Pin Code (some digit Number [0-9])
 0B: User Name (available ISO 8859-1 character set)
 0C: Password (available ISO 8859-1 character set)
 */
public static final String NET_KEYBOARD_LL = "net-keyboard::ll";
// public static final String NET_KEYBOARD_LL_ISCP = "NKYll";

/**
 * ISCP Command: NKYnnnnnnnnn
 * set Keyboard Input letter
"nnnnnnnn" is variable-length, 128 Unicode letters [UTF-8 encoded] max
 */
public static final String NET_KEYBOARD_NULL = "net-keyboard::null";
// public static final String NET_KEYBOARD_NULL_ISCP = "NKYnnnnnnnnn";

/**
 * CommandBlock
 * ISCP Command prefix: NPU
 * NET Popup Message(for Network Control Only)
 */
public static final String NET_POPUP_MESSAGE = "net-popup-message";
public static final String NET_POPUP_MESSAGE_ISCP = "NPU";

/**
 * ISCP Command: NPUxaaa?aaaybbb?bbb
 * x -> Popup Display Type
 'T': Popup text is top
 'B': Popup text is bottom
 'L': Popup text is list format

aaa...aaa -> Popup Title, Massage
 when x = 'T' or 'B'
    Top Title [0x00] Popup Title [0x00] Popup Message [0x00]
    (valiable-length Unicode letter [UTF-8 encoded] )

 when x = 'L'
    Top Title [0x00] Item Title 1 [0x00] Item Parameter 1 [0x00] ... [0x00] Item Title 6 [0x00] Item Parameter 6 [0x00]
    (valiable-length Unicode letter [UTF-8 encoded] )

y -> Cursor Position on button
 '0' : Button is not Displayed
 '1' : Cursor is on the button 1
 '2' : Cursor is on the button 2

bbb...bbb -> Text of Button
    Text of Button 1 [0x00] Text of Button 2 [0x00]
    (valiable-length Unicode letter [UTF-8 encoded] )
 */
public static final String NET_POPUP_MESSAGE_NULL = "net-popup-message::null";
// public static final String NET_POPUP_MESSAGE_NULL_ISCP = "NPUxaaa?aaaybbb?bbb";

/**
 * CommandBlock
 * ISCP Command prefix: NSV
 * NET Service(for Network Control Only)
 */
public static final String NET_SERVICE = "net-service";
public static final String NET_SERVICE_ISCP = "NSV";

/**
 * ISCP Command: NSVssiaaaa?aaaabbbb?bbbb
 * select Network Service directly
ss -> Network Serveice
 00:Media Server (DLNA)
 01:Favorite
 02:vTuner
 03:SIRIUS
 04:Pandora
 05:Rhapsody
 06:Last.fm
 07:Napster
 08:Slacker
 09:Mediafly
 0A:Spotify
 0B:AUPEO!
 0C:Radiko
 0D:e-onkyo

i-> Acount Info
 0: No
 1: Yes
"aaaa...aaaa": User Name ( 128 Unicode letters [UTF-8 encoded] max )
"bbbb...bbbb": Password ( 128 Unicode letters [UTF-8 encoded] max )
 */
public static final String NET_SERVICE_NULL = "net-service::null";
// public static final String NET_SERVICE_NULL_ISCP = "NSVssiaaaa?aaaabbbb?bbbb";

/**
 * CommandBlock
 * ISCP Command prefix: NAL
 * NET/USB Album Name Info
 */
public static final String NET_USB_ALBUM_NAME_INFO = "net-usb-album-name-info";
public static final String NET_USB_ALBUM_NAME_INFO_ISCP = "NAL";

/**
 * ISCP Command: NALnnnnnnn
 * NET/USB Album Name (variable-length, 64 Unicode letters [UTF-8 encoded] max , for Network Control only)
 */
public static final String NET_USB_ALBUM_NAME_INFO_NULL = "net-usb-album-name-info::null";
// public static final String NET_USB_ALBUM_NAME_INFO_NULL_ISCP = "NALnnnnnnn";

/**
 * ISCP Command: NALQSTN
 * gets iPod Album Name
 */
public static final String NET_USB_ALBUM_NAME_INFO_QUERY = "net-usb-album-name-info::query";
// public static final String NET_USB_ALBUM_NAME_INFO_QUERY_ISCP = "NALQSTN";

/**
 * CommandBlock
 * ISCP Command prefix: NAT
 * NET/USB Artist Name Info
 */
public static final String NET_USB_ARTIST_NAME_INFO = "net-usb-artist-name-info";
public static final String NET_USB_ARTIST_NAME_INFO_ISCP = "NAT";

/**
 * ISCP Command: NATnnnnnnnnnn
 * NET/USB Artist Name (variable-length, 64 Unicode letters [UTF-8 encoded] max , for Network Control only)
 */
public static final String NET_USB_ARTIST_NAME_INFO_NULL = "net-usb-artist-name-info::null";
// public static final String NET_USB_ARTIST_NAME_INFO_NULL_ISCP = "NATnnnnnnnnnn";

/**
 * ISCP Command: NATQSTN
 * gets iPod Artist Name
 */
public static final String NET_USB_ARTIST_NAME_INFO_QUERY = "net-usb-artist-name-info::query";
// public static final String NET_USB_ARTIST_NAME_INFO_QUERY_ISCP = "NATQSTN";

/**
 * CommandBlock
 * ISCP Command prefix: NJA
 * NET/USB Jacket Art (When Jacket Art is available and Output for Network Control Only)
 */
public static final String NET_USB_JACKET_ART = "net-usb-jacket-art";
public static final String NET_USB_JACKET_ART_ISCP = "NJA";

/**
 * ISCP Command: NJAtp{xx}{xx}{xx}{xx}{xx}{xx}
 * NET/USB Jacket Art/Album Art Data
t-> Image type 0:BMP,1:JPEG
p-> Packet flag 0:Start, 1:Next, 2:End
xxxxxxxxxxxxxx -> Jacket/Album Art Data (valiable length, 1024 ASCII HEX letters max)
 */
public static final String NET_USB_JACKET_ART_TP_XX_XX_XX_XX_XX_XX = "net-usb-jacket-art::tp-xx-xx-xx-xx-xx-xx";
// public static final String NET_USB_JACKET_ART_TP_XX_XX_XX_XX_XX_XX_ISCP = "NJAtp{xx}{xx}{xx}{xx}{xx}{xx}";

/**
 * CommandBlock
 * ISCP Command prefix: NLS
 * NET/USB List Info
 */
public static final String NET_USB_LIST_INFO = "net-usb-list-info";
public static final String NET_USB_LIST_INFO_ISCP = "NLS";

/**
 * ISCP Command: NLStlpnnnnnnnnnn
 * NET/USB List Info
t ->Information Type (A : ASCII letter, C : Cursor Info, U : Unicode letter)
when t = A,
  l ->Line Info (0-9 : 1st to 10th Line)
  nnnnnnnnn:Listed data (variable-length, 64 ASCII letters max)
    when AVR is not displayed NET/USB List(Ketboard,Menu,Popup?), "nnnnnnnnn" is "See TV".
  p ->Property (- : no)
when t = C,
  l ->Cursor Position (0-9 : 1st to 10th Line, - : No Cursor)
  p ->Update Type (P : Page Infomation Update ( Page Clear or Disable List Info) , C : Cursor Position Update)
when t = U, (for Network Control Only)
  l ->Line Info (0-9 : 1st to 10th Line)
  nnnnnnnnn:Listed data (variable-length, 64 Unicode letters [UTF-8 encoded] max)
    when AVR is not displayed NET/USB List(Ketboard,Menu,Popup?), "nnnnnnnnn" is "See TV".
  p ->Property (- : no)
 */
public static final String NET_USB_LIST_INFO_NULL = "net-usb-list-info::null";
// public static final String NET_USB_LIST_INFO_NULL_ISCP = "NLStlpnnnnnnnnnn";

/**
 * ISCP Command: NLSti
 * select the listed item (from Network Control Only)
 t -> Index Type (L : Line, I : Index)
when t = L,
  i -> Line number (0-9 : 1st to 10th Line [1 digit] )
when t = I,
  iiiii -> Index number (00001-99999 : 1st to 99999th Item [5 digits] )
 */
public static final String NET_USB_LIST_INFO_TI = "net-usb-list-info::ti";
// public static final String NET_USB_LIST_INFO_TI_ISCP = "NLSti";

/**
 * CommandBlock
 * ISCP Command prefix: NST
 * NET/USB Play Status
 */
public static final String NET_USB_PLAY_STATUS = "net-usb-play-status";
public static final String NET_USB_PLAY_STATUS_ISCP = "NST";

/**
 * ISCP Command: NSTprs
 * NET/USB Play Status (3 letters)
p -> Play Status: "S": STOP, "P": Play, "p": Pause, "F": FF, "R": FR
r -> Repeat Status: "-": Off, "R": All, "F": Folder, "1": Repeat 1,
s -> Shuffle Status: "-": Off, "S": All , "A": Album, "F": Folder
 */
public static final String NET_USB_PLAY_STATUS_PRS = "net-usb-play-status::prs";
// public static final String NET_USB_PLAY_STATUS_PRS_ISCP = "NSTprs";

/**
 * ISCP Command: NSTQSTN
 * gets the Net/USB Status
 */
public static final String NET_USB_PLAY_STATUS_QUERY = "net-usb-play-status::query";
// public static final String NET_USB_PLAY_STATUS_QUERY_ISCP = "NSTQSTN";

/**
 * CommandBlock
 * ISCP Command prefix: NTM
 * NET/USB Time Info
 */
public static final String NET_USB_TIME_INFO = "net-usb-time-info";
public static final String NET_USB_TIME_INFO_ISCP = "NTM";

/**
 * ISCP Command: NTMmm:ss/mm:ss
 * NET/USB Time Info (Elapsed time/Track Time Max 99:59)
 */
public static final String NET_USB_TIME_INFO_MM_SS_MM_SS = "net-usb-time-info::mm-ss-mm-ss";
// public static final String NET_USB_TIME_INFO_MM_SS_MM_SS_ISCP = "NTMmm:ss/mm:ss";

/**
 * ISCP Command: NTMQSTN
 * gets iPod Time Info
 */
public static final String NET_USB_TIME_INFO_QUERY = "net-usb-time-info::query";
// public static final String NET_USB_TIME_INFO_QUERY_ISCP = "NTMQSTN";

/**
 * CommandBlock
 * ISCP Command prefix: NTI
 * NET/USB Title Name
 */
public static final String NET_USB_TITLE_NAME = "net-usb-title-name";
public static final String NET_USB_TITLE_NAME_ISCP = "NTI";

/**
 * ISCP Command: NTInnnnnnnnnn
 * NET/USB Title Name (variable-length, 64 Unicode letters [UTF-8 encoded] max , for Network Control only)
 */
public static final String NET_USB_TITLE_NAME_NULL = "net-usb-title-name::null";
// public static final String NET_USB_TITLE_NAME_NULL_ISCP = "NTInnnnnnnnnn";

/**
 * ISCP Command: NTIQSTN
 * gets HD Radio Title
 */
public static final String NET_USB_TITLE_NAME_QUERY = "net-usb-title-name::query";
// public static final String NET_USB_TITLE_NAME_QUERY_ISCP = "NTIQSTN";

/**
 * CommandBlock
 * ISCP Command prefix: NTR
 * NET/USB Track Info
 */
public static final String NET_USB_TRACK_INFO = "net-usb-track-info";
public static final String NET_USB_TRACK_INFO_ISCP = "NTR";

/**
 * ISCP Command: NTRcccc/tttt
 * NET/USB Track Info (Current Track/Toral Track Max 9999)
 */
public static final String NET_USB_TRACK_INFO_CCCC_TTTT = "net-usb-track-info::cccc-tttt";
// public static final String NET_USB_TRACK_INFO_CCCC_TTTT_ISCP = "NTRcccc/tttt";

/**
 * ISCP Command: NTRQSTN
 * gets iPod Time Info
 */
public static final String NET_USB_TRACK_INFO_QUERY = "net-usb-track-info::query";
// public static final String NET_USB_TRACK_INFO_QUERY_ISCP = "NTRQSTN";

/**
 * CommandBlock
 * ISCP Command prefix: NTC
 * Network/USB Operation Command (Network Model Only after TX-NR905)
 */
public static final String NETWORK_USB = "network-usb";
public static final String NETWORK_USB_ISCP = "NTC";

/**
 * ISCP Command: NTC0
 * 0 KEY
 */
public static final String NETWORK_USB_0 = "network-usb::0";
// public static final String NETWORK_USB_0_ISCP = "NTC0";

/**
 * ISCP Command: NTC1
 * 1 KEY
 */
public static final String NETWORK_USB_1 = "network-usb::1";
// public static final String NETWORK_USB_1_ISCP = "NTC1";

/**
 * ISCP Command: NTC2
 * 2 KEY
 */
public static final String NETWORK_USB_2 = "network-usb::2";
// public static final String NETWORK_USB_2_ISCP = "NTC2";

/**
 * ISCP Command: NTC3
 * 3 KEY
 */
public static final String NETWORK_USB_3 = "network-usb::3";
// public static final String NETWORK_USB_3_ISCP = "NTC3";

/**
 * ISCP Command: NTC4
 * 4 KEY
 */
public static final String NETWORK_USB_4 = "network-usb::4";
// public static final String NETWORK_USB_4_ISCP = "NTC4";

/**
 * ISCP Command: NTC5
 * 5 KEY
 */
public static final String NETWORK_USB_5 = "network-usb::5";
// public static final String NETWORK_USB_5_ISCP = "NTC5";

/**
 * ISCP Command: NTC6
 * 6 KEY
 */
public static final String NETWORK_USB_6 = "network-usb::6";
// public static final String NETWORK_USB_6_ISCP = "NTC6";

/**
 * ISCP Command: NTC7
 * 7 KEY
 */
public static final String NETWORK_USB_7 = "network-usb::7";
// public static final String NETWORK_USB_7_ISCP = "NTC7";

/**
 * ISCP Command: NTC8
 * 8 KEY
 */
public static final String NETWORK_USB_8 = "network-usb::8";
// public static final String NETWORK_USB_8_ISCP = "NTC8";

/**
 * ISCP Command: NTC9
 * 9 KEY
 */
public static final String NETWORK_USB_9 = "network-usb::9";
// public static final String NETWORK_USB_9_ISCP = "NTC9";

/**
 * ISCP Command: NTCALBUM
 * ALBUM KEY
 */
public static final String NETWORK_USB_ALBUM = "network-usb::album";
// public static final String NETWORK_USB_ALBUM_ISCP = "NTCALBUM";

/**
 * ISCP Command: NTCARTIST
 * ARTIST KEY
 */
public static final String NETWORK_USB_ARTIST = "network-usb::artist";
// public static final String NETWORK_USB_ARTIST_ISCP = "NTCARTIST";

/**
 * ISCP Command: NTCCAPS
 * CAPS KEY
 */
public static final String NETWORK_USB_CAPS = "network-usb::caps";
// public static final String NETWORK_USB_CAPS_ISCP = "NTCCAPS";

/**
 * ISCP Command: NTCCHDN
 * CH DOWN(for iRadio)
 */
public static final String NETWORK_USB_CHDN = "network-usb::chdn";
// public static final String NETWORK_USB_CHDN_ISCP = "NTCCHDN";

/**
 * ISCP Command: NTCCHUP
 * CH UP(for iRadio)
 */
public static final String NETWORK_USB_CHUP = "network-usb::chup";
// public static final String NETWORK_USB_CHUP_ISCP = "NTCCHUP";

/**
 * ISCP Command: NTCDELETE
 * DELETE KEY
 */
public static final String NETWORK_USB_DELETE = "network-usb::delete";
// public static final String NETWORK_USB_DELETE_ISCP = "NTCDELETE";

/**
 * ISCP Command: NTCDISPLAY
 * DISPLAY KEY
 */
public static final String NETWORK_USB_DISPLAY = "network-usb::display";
// public static final String NETWORK_USB_DISPLAY_ISCP = "NTCDISPLAY";

/**
 * ISCP Command: NTCDOWN
 * DOWN KEY
 */
public static final String NETWORK_USB_DOWN = "network-usb::down";
// public static final String NETWORK_USB_DOWN_ISCP = "NTCDOWN";

/**
 * ISCP Command: NTCFF
 * FF KEY (CONTINUOUS*)
 */
public static final String NETWORK_USB_FF = "network-usb::ff";
// public static final String NETWORK_USB_FF_ISCP = "NTCFF";

/**
 * ISCP Command: NTCGENRE
 * GENRE KEY
 */
public static final String NETWORK_USB_GENRE = "network-usb::genre";
// public static final String NETWORK_USB_GENRE_ISCP = "NTCGENRE";

/**
 * ISCP Command: NTCLANGUAGE
 * LANGUAGE KEY
 */
public static final String NETWORK_USB_LANGUAGE = "network-usb::language";
// public static final String NETWORK_USB_LANGUAGE_ISCP = "NTCLANGUAGE";

/**
 * ISCP Command: NTCLEFT
 * LEFT KEY
 */
public static final String NETWORK_USB_LEFT = "network-usb::left";
// public static final String NETWORK_USB_LEFT_ISCP = "NTCLEFT";

/**
 * ISCP Command: NTCLIST
 * LIST <-> PLAYBACK
 */
public static final String NETWORK_USB_LIST = "network-usb::list";
// public static final String NETWORK_USB_LIST_ISCP = "NTCLIST";

/**
 * ISCP Command: NTCLOCATION
 * LOCATION KEY
 */
public static final String NETWORK_USB_LOCATION = "network-usb::location";
// public static final String NETWORK_USB_LOCATION_ISCP = "NTCLOCATION";

/**
 * ISCP Command: NTCMENU
 * MENU
 */
public static final String NETWORK_USB_MENU = "network-usb::menu";
// public static final String NETWORK_USB_MENU_ISCP = "NTCMENU";

/**
 * ISCP Command: NTCMODE
 * MODE(for iPod) STD<->EXT
 */
public static final String NETWORK_USB_MODE = "network-usb::mode";
// public static final String NETWORK_USB_MODE_ISCP = "NTCMODE";

/**
 * ISCP Command: NTCPAUSE
 * PAUSE KEY
 */
public static final String NETWORK_USB_PAUSE = "network-usb::pause";
// public static final String NETWORK_USB_PAUSE_ISCP = "NTCPAUSE";

/**
 * ISCP Command: NTCPLAY
 * PLAY KEY
 */
public static final String NETWORK_USB_PLAY = "network-usb::play";
// public static final String NETWORK_USB_PLAY_ISCP = "NTCPLAY";

/**
 * ISCP Command: NTCPLAYLIST
 * PLAYLIST KEY
 */
public static final String NETWORK_USB_PLAYLIST = "network-usb::playlist";
// public static final String NETWORK_USB_PLAYLIST_ISCP = "NTCPLAYLIST";

/**
 * ISCP Command: NTCRANDOM
 * RANDOM KEY
 */
public static final String NETWORK_USB_RANDOM = "network-usb::random";
// public static final String NETWORK_USB_RANDOM_ISCP = "NTCRANDOM";

/**
 * ISCP Command: NTCREPEAT
 * REPEAT KEY
 */
public static final String NETWORK_USB_REPEAT = "network-usb::repeat";
// public static final String NETWORK_USB_REPEAT_ISCP = "NTCREPEAT";

/**
 * ISCP Command: NTCRETURN
 * RETURN KEY
 */
public static final String NETWORK_USB_RETURN = "network-usb::return";
// public static final String NETWORK_USB_RETURN_ISCP = "NTCRETURN";

/**
 * ISCP Command: NTCREW
 * REW KEY (CONTINUOUS*)
 */
public static final String NETWORK_USB_REW = "network-usb::rew";
// public static final String NETWORK_USB_REW_ISCP = "NTCREW";

/**
 * ISCP Command: NTCRIGHT
 * RIGHT KEY
 */
public static final String NETWORK_USB_RIGHT = "network-usb::right";
// public static final String NETWORK_USB_RIGHT_ISCP = "NTCRIGHT";

/**
 * ISCP Command: NTCSELECT
 * SELECT KEY
 */
public static final String NETWORK_USB_SELECT = "network-usb::select";
// public static final String NETWORK_USB_SELECT_ISCP = "NTCSELECT";

/**
 * ISCP Command: NTCSETUP
 * SETUP KEY
 */
public static final String NETWORK_USB_SETUP = "network-usb::setup";
// public static final String NETWORK_USB_SETUP_ISCP = "NTCSETUP";

/**
 * ISCP Command: NTCSTOP
 * STOP KEY
 */
public static final String NETWORK_USB_STOP = "network-usb::stop";
// public static final String NETWORK_USB_STOP_ISCP = "NTCSTOP";

/**
 * ISCP Command: NTCTOP
 * TOP MENU
 */
public static final String NETWORK_USB_TOP = "network-usb::top";
// public static final String NETWORK_USB_TOP_ISCP = "NTCTOP";

/**
 * ISCP Command: NTCTRDN
 * TRACK DOWN KEY
 */
public static final String NETWORK_USB_TRDN = "network-usb::trdn";
// public static final String NETWORK_USB_TRDN_ISCP = "NTCTRDN";

/**
 * ISCP Command: NTCTRUP
 * TRACK UP KEY
 */
public static final String NETWORK_USB_TRUP = "network-usb::trup";
// public static final String NETWORK_USB_TRUP_ISCP = "NTCTRUP";

/**
 * ISCP Command: NTCUP
 * UP KEY
 */
public static final String NETWORK_USB_UP = "network-usb::up";
// public static final String NETWORK_USB_UP_ISCP = "NTCUP";

/**
 * CommandBlock
 * ISCP Command prefix: PRS
 * Preset Command (Include Tuner Pack Model Only)
 */
public static final String PRESET = "preset";
public static final String PRESET_ISCP = "PRS";

/**
 * ISCP Command: PRSDOWN
 * sets Preset No. Wrap-Around Down
 */
public static final String PRESET_DOWN = "preset::down";
// public static final String PRESET_DOWN_ISCP = "PRSDOWN";

/**
 * ISCP Command: PRSQSTN
 * gets The Preset No.
 */
public static final String PRESET_QUERY = "preset::query";
// public static final String PRESET_QUERY_ISCP = "PRSQSTN";

/**
 * ISCP Command: PRSUP
 * sets Preset No. Wrap-Around Up
 */
public static final String PRESET_UP = "preset::up";
// public static final String PRESET_UP_ISCP = "PRSUP";

/**
 * CommandBlock
 * ISCP Command prefix: UPM
 * Preset Memory Command (Universal Port Dock Only)
 */
public static final String PRESET_MEMORY = "preset-memory";
public static final String PRESET_MEMORY_ISCP = "UPM";

/**
 * CommandBlock
 * ISCP Command prefix: PTS
 * PTY Scan Command (RDS Model Only)
 */
public static final String PTY_SCAN = "pty-scan";
public static final String PTY_SCAN_ISCP = "PTS";

/**
 * ISCP Command: PTSENTER
 * Finish PTY Scan
 */
public static final String PTY_SCAN_ENTER = "pty-scan::enter";
// public static final String PTY_SCAN_ENTER_ISCP = "PTSENTER";

/**
 * CommandBlock
 * ISCP Command prefix: RDS
 * RDS Information Command (RDS Model Only)
 */
public static final String RDS_INFORMATION = "rds-information";
public static final String RDS_INFORMATION_ISCP = "RDS";

/**
 * ISCP Command: RDS00
 * Display RT Information
 */
public static final String RDS_INFORMATION_00 = "rds-information::00";
// public static final String RDS_INFORMATION_00_ISCP = "RDS00";

/**
 * ISCP Command: RDS01
 * Display PTY Information
 */
public static final String RDS_INFORMATION_01 = "rds-information::01";
// public static final String RDS_INFORMATION_01_ISCP = "RDS01";

/**
 * ISCP Command: RDS02
 * Display TP Information
 */
public static final String RDS_INFORMATION_02 = "rds-information::02";
// public static final String RDS_INFORMATION_02_ISCP = "RDS02";

/**
 * ISCP Command: RDSUP
 * Display RDS Information Wrap-Around Change
 */
public static final String RDS_INFORMATION_UP = "rds-information::up";
// public static final String RDS_INFORMATION_UP_ISCP = "RDSUP";

/**
 * CommandBlock
 * ISCP Command prefix: SLR
 * RECOUT Selector Command
 */
public static final String RECOUT_SELECTOR = "recout-selector";
public static final String RECOUT_SELECTOR_ISCP = "SLR";

/**
 * ISCP Command: SLR25
 * sets AM
 */
public static final String RECOUT_SELECTOR_AM = "recout-selector::am";
// public static final String RECOUT_SELECTOR_AM_ISCP = "SLR25";

/**
 * ISCP Command: SLR23
 * sets CD
 */
public static final String RECOUT_SELECTOR_CD = "recout-selector::cd";
// public static final String RECOUT_SELECTOR_CD_ISCP = "SLR23";

/**
 * ISCP Command: SLR10
 * sets DVD
 */
public static final String RECOUT_SELECTOR_DVD = "recout-selector::dvd";
// public static final String RECOUT_SELECTOR_DVD_ISCP = "SLR10";

/**
 * ISCP Command: SLR24
 * sets FM
 */
public static final String RECOUT_SELECTOR_FM = "recout-selector::fm";
// public static final String RECOUT_SELECTOR_FM_ISCP = "SLR24";

/**
 * ISCP Command: SLR28
 * sets INTERNET RADIO
 */
public static final String RECOUT_SELECTOR_INTERNET_RADIO = "recout-selector::internet-radio";
// public static final String RECOUT_SELECTOR_INTERNET_RADIO_ISCP = "SLR28";

/**
 * ISCP Command: SLR30
 * sets MULTI CH
 */
public static final String RECOUT_SELECTOR_MULTI_CH = "recout-selector::multi-ch";
// public static final String RECOUT_SELECTOR_MULTI_CH_ISCP = "SLR30";

/**
 * ISCP Command: SLR27
 * sets MUSIC SERVER
 */
public static final String RECOUT_SELECTOR_MUSIC_SERVER = "recout-selector::music-server";
// public static final String RECOUT_SELECTOR_MUSIC_SERVER_ISCP = "SLR27";

/**
 * ISCP Command: SLR7F
 * sets OFF
 */
public static final String RECOUT_SELECTOR_OFF = "recout-selector::off";
// public static final String RECOUT_SELECTOR_OFF_ISCP = "SLR7F";

/**
 * ISCP Command: SLR22
 * sets PHONO
 */
public static final String RECOUT_SELECTOR_PHONO = "recout-selector::phono";
// public static final String RECOUT_SELECTOR_PHONO_ISCP = "SLR22";

/**
 * ISCP Command: SLRQSTN
 * gets The Selector Position
 */
public static final String RECOUT_SELECTOR_QUERY = "recout-selector::query";
// public static final String RECOUT_SELECTOR_QUERY_ISCP = "SLRQSTN";

/**
 * ISCP Command: SLR80
 * sets SOURCE
 */
public static final String RECOUT_SELECTOR_SOURCE = "recout-selector::source";
// public static final String RECOUT_SELECTOR_SOURCE_ISCP = "SLR80";

/**
 * ISCP Command: SLR20
 * sets TAPE(1)
 */
public static final String RECOUT_SELECTOR_TAPE = "recout-selector::tape";
// public static final String RECOUT_SELECTOR_TAPE_ISCP = "SLR20";

/**
 * ISCP Command: SLR21
 * sets TAPE2
 */
public static final String RECOUT_SELECTOR_TAPE2 = "recout-selector::tape2";
// public static final String RECOUT_SELECTOR_TAPE2_ISCP = "SLR21";

/**
 * ISCP Command: SLR26
 * sets TUNER
 */
public static final String RECOUT_SELECTOR_TUNER = "recout-selector::tuner";
// public static final String RECOUT_SELECTOR_TUNER_ISCP = "SLR26";

/**
 * ISCP Command: SLR00
 * sets VIDEO1
 */
public static final String RECOUT_SELECTOR_VIDEO1 = "recout-selector::video1";
// public static final String RECOUT_SELECTOR_VIDEO1_ISCP = "SLR00";

/**
 * ISCP Command: SLR01
 * sets VIDEO2
 */
public static final String RECOUT_SELECTOR_VIDEO2 = "recout-selector::video2";
// public static final String RECOUT_SELECTOR_VIDEO2_ISCP = "SLR01";

/**
 * ISCP Command: SLR02
 * sets VIDEO3
 */
public static final String RECOUT_SELECTOR_VIDEO3 = "recout-selector::video3";
// public static final String RECOUT_SELECTOR_VIDEO3_ISCP = "SLR02";

/**
 * ISCP Command: SLR03
 * sets VIDEO4
 */
public static final String RECOUT_SELECTOR_VIDEO4 = "recout-selector::video4";
// public static final String RECOUT_SELECTOR_VIDEO4_ISCP = "SLR03";

/**
 * ISCP Command: SLR04
 * sets VIDEO5
 */
public static final String RECOUT_SELECTOR_VIDEO5 = "recout-selector::video5";
// public static final String RECOUT_SELECTOR_VIDEO5_ISCP = "SLR04";

/**
 * ISCP Command: SLR05
 * sets VIDEO6
 */
public static final String RECOUT_SELECTOR_VIDEO6 = "recout-selector::video6";
// public static final String RECOUT_SELECTOR_VIDEO6_ISCP = "SLR05";

/**
 * ISCP Command: SLR06
 * sets VIDEO7
 */
public static final String RECOUT_SELECTOR_VIDEO7 = "recout-selector::video7";
// public static final String RECOUT_SELECTOR_VIDEO7_ISCP = "SLR06";

/**
 * ISCP Command: SLR31
 * sets XM
 */
public static final String RECOUT_SELECTOR_XM = "recout-selector::xm";
// public static final String RECOUT_SELECTOR_XM_ISCP = "SLR31";

/**
 * CommandBlock
 * ISCP Command prefix: OSD
 * Setup Operation Command
 */
public static final String SETUP = "setup";
public static final String SETUP_ISCP = "OSD";

/**
 * ISCP Command: OSDAUDIO
 * Audio Adjust Key
 */
public static final String SETUP_AUDIO = "setup::audio";
// public static final String SETUP_AUDIO_ISCP = "OSDAUDIO";

/**
 * ISCP Command: OSDDOWN
 * Down Key
 */
public static final String SETUP_DOWN = "setup::down";
// public static final String SETUP_DOWN_ISCP = "OSDDOWN";

/**
 * ISCP Command: OSDENTER
 * Enter Key
 */
public static final String SETUP_ENTER = "setup::enter";
// public static final String SETUP_ENTER_ISCP = "OSDENTER";

/**
 * ISCP Command: OSDEXIT
 * Exit Key
 */
public static final String SETUP_EXIT = "setup::exit";
// public static final String SETUP_EXIT_ISCP = "OSDEXIT";

/**
 * ISCP Command: OSDHOME
 * Home Key
 */
public static final String SETUP_HOME = "setup::home";
// public static final String SETUP_HOME_ISCP = "OSDHOME";

/**
 * ISCP Command: OSDLEFT
 * Left Key
 */
public static final String SETUP_LEFT = "setup::left";
// public static final String SETUP_LEFT_ISCP = "OSDLEFT";

/**
 * ISCP Command: OSDMENU
 * Menu Key
 */
public static final String SETUP_MENU = "setup::menu";
// public static final String SETUP_MENU_ISCP = "OSDMENU";

/**
 * ISCP Command: OSDRIGHT
 * Right Key
 */
public static final String SETUP_RIGHT = "setup::right";
// public static final String SETUP_RIGHT_ISCP = "OSDRIGHT";

/**
 * ISCP Command: OSDUP
 * Up Key
 */
public static final String SETUP_UP = "setup::up";
// public static final String SETUP_UP_ISCP = "OSDUP";

/**
 * ISCP Command: OSDVIDEO
 * Video Adjust Key
 */
public static final String SETUP_VIDEO = "setup::video";
// public static final String SETUP_VIDEO_ISCP = "OSDVIDEO";

/**
 * CommandBlock
 * ISCP Command prefix: SAT
 * SIRIUS Artist Name Info (SIRIUS Model Only)
 */
public static final String SIRIUS_ARTIST_NAME_INFO = "sirius-artist-name-info";
public static final String SIRIUS_ARTIST_NAME_INFO_ISCP = "SAT";

/**
 * ISCP Command: SATnnnnnnnnnn
 * SIRIUS Artist Name
 */
public static final String SIRIUS_ARTIST_NAME_INFO_NULL = "sirius-artist-name-info::null";
// public static final String SIRIUS_ARTIST_NAME_INFO_NULL_ISCP = "SATnnnnnnnnnn";

/**
 * ISCP Command: SATQSTN
 * gets SIRIUS Artist Name
 */
public static final String SIRIUS_ARTIST_NAME_INFO_QUERY = "sirius-artist-name-info::query";
// public static final String SIRIUS_ARTIST_NAME_INFO_QUERY_ISCP = "SATQSTN";

/**
 * CommandBlock
 * ISCP Command prefix: SCT
 * SIRIUS Category Command (SIRIUS Model Only)
 */
public static final String SIRIUS_CATEGORY = "sirius-category";
public static final String SIRIUS_CATEGORY_ISCP = "SCT";

/**
 * ISCP Command: SCTDOWN
 * sets SIRIUS Category Wrap-Around Down
 */
public static final String SIRIUS_CATEGORY_DOWN = "sirius-category::down";
// public static final String SIRIUS_CATEGORY_DOWN_ISCP = "SCTDOWN";

/**
 * ISCP Command: SCTnnnnnnnnnn
 * SIRIUS Category Info
 */
public static final String SIRIUS_CATEGORY_NULL = "sirius-category::null";
// public static final String SIRIUS_CATEGORY_NULL_ISCP = "SCTnnnnnnnnnn";

/**
 * ISCP Command: SCTQSTN
 * gets SIRIUS Category
 */
public static final String SIRIUS_CATEGORY_QUERY = "sirius-category::query";
// public static final String SIRIUS_CATEGORY_QUERY_ISCP = "SCTQSTN";

/**
 * ISCP Command: SCTUP
 * sets SIRIUS Category Wrap-Around Up
 */
public static final String SIRIUS_CATEGORY_UP = "sirius-category::up";
// public static final String SIRIUS_CATEGORY_UP_ISCP = "SCTUP";

/**
 * CommandBlock
 * ISCP Command prefix: SCN
 * SIRIUS Channel Name Info (SIRIUS Model Only)
 */
public static final String SIRIUS_CHANNEL_NAME_INFO = "sirius-channel-name-info";
public static final String SIRIUS_CHANNEL_NAME_INFO_ISCP = "SCN";

/**
 * ISCP Command: SCNnnnnnnnnnn
 * SIRIUS Channel Name
 */
public static final String SIRIUS_CHANNEL_NAME_INFO_NULL = "sirius-channel-name-info::null";
// public static final String SIRIUS_CHANNEL_NAME_INFO_NULL_ISCP = "SCNnnnnnnnnnn";

/**
 * ISCP Command: SCNQSTN
 * gets SIRIUS Channel Name
 */
public static final String SIRIUS_CHANNEL_NAME_INFO_QUERY = "sirius-channel-name-info::query";
// public static final String SIRIUS_CHANNEL_NAME_INFO_QUERY_ISCP = "SCNQSTN";

/**
 * CommandBlock
 * ISCP Command prefix: SCH
 * SIRIUS Channel Number Command (SIRIUS Model Only)
 */
public static final String SIRIUS_CHANNEL_NUMBER = "sirius-channel-number";
public static final String SIRIUS_CHANNEL_NUMBER_ISCP = "SCH";

/**
 * ISCP Command: SCHDOWN
 * sets SIRIUS Channel Wrap-Around Down
 */
public static final String SIRIUS_CHANNEL_NUMBER_DOWN = "sirius-channel-number::down";
// public static final String SIRIUS_CHANNEL_NUMBER_DOWN_ISCP = "SCHDOWN";

/**
 * ISCP Command: SCHQSTN
 * gets SIRIUS Channel Number
 */
public static final String SIRIUS_CHANNEL_NUMBER_QUERY = "sirius-channel-number::query";
// public static final String SIRIUS_CHANNEL_NUMBER_QUERY_ISCP = "SCHQSTN";

/**
 * ISCP Command: SCHUP
 * sets SIRIUS Channel Wrap-Around Up
 */
public static final String SIRIUS_CHANNEL_NUMBER_UP = "sirius-channel-number::up";
// public static final String SIRIUS_CHANNEL_NUMBER_UP_ISCP = "SCHUP";

/**
 * CommandBlock
 * ISCP Command prefix: SLK
 * SIRIUS Parental Lock Command (SIRIUS Model Only)
 */
public static final String SIRIUS_PARENTAL_LOCK = "sirius-parental-lock";
public static final String SIRIUS_PARENTAL_LOCK_ISCP = "SLK";

/**
 * ISCP Command: SLKINPUT
 * displays "Please input the Lock password"
 */
public static final String SIRIUS_PARENTAL_LOCK_INPUT = "sirius-parental-lock::input";
// public static final String SIRIUS_PARENTAL_LOCK_INPUT_ISCP = "SLKINPUT";

/**
 * ISCP Command: SLKnnnn
 * Lock Password (4Digits)
 */
public static final String SIRIUS_PARENTAL_LOCK_NULL = "sirius-parental-lock::null";
// public static final String SIRIUS_PARENTAL_LOCK_NULL_ISCP = "SLKnnnn";

/**
 * ISCP Command: SLKWRONG
 * displays "The Lock password is wrong"
 */
public static final String SIRIUS_PARENTAL_LOCK_WRONG = "sirius-parental-lock::wrong";
// public static final String SIRIUS_PARENTAL_LOCK_WRONG_ISCP = "SLKWRONG";

/**
 * CommandBlock
 * ISCP Command prefix: STI
 * SIRIUS Title Info (SIRIUS Model Only)
 */
public static final String SIRIUS_TITLE_INFO = "sirius-title-info";
public static final String SIRIUS_TITLE_INFO_ISCP = "STI";

/**
 * ISCP Command: STInnnnnnnnnn
 * SIRIUS Title
 */
public static final String SIRIUS_TITLE_INFO_NULL = "sirius-title-info::null";
// public static final String SIRIUS_TITLE_INFO_NULL_ISCP = "STInnnnnnnnnn";

/**
 * ISCP Command: STIQSTN
 * gets SIRIUS Title
 */
public static final String SIRIUS_TITLE_INFO_QUERY = "sirius-title-info::query";
// public static final String SIRIUS_TITLE_INFO_QUERY_ISCP = "STIQSTN";

/**
 * CommandBlock
 * ISCP Command prefix: SLP
 * Sleep Set Command
 */
public static final String SLEEP_SET = "sleep-set";
public static final String SLEEP_SET_ISCP = "SLP";

/**
 * ISCP Command: SLPQSTN
 * gets The Sleep Time
 */
public static final String SLEEP_SET_QUERY = "sleep-set::query";
// public static final String SLEEP_SET_QUERY_ISCP = "SLPQSTN";

/**
 * ISCP Command: SLPOFF
 * sets Sleep Time Off
 */
public static final String SLEEP_SET_TIME_OFF = "sleep-set::time-off";
// public static final String SLEEP_SET_TIME_OFF_ISCP = "SLPOFF";

/**
 * ISCP Command: SLPUP
 * sets Sleep Time Wrap-Around UP
 */
public static final String SLEEP_SET_UP = "sleep-set::up";
// public static final String SLEEP_SET_UP_ISCP = "SLPUP";

/**
 * CommandBlock
 * ISCP Command prefix: SPA
 * Speaker A Command
 */
public static final String SPEAKER_A = "speaker-a";
public static final String SPEAKER_A_ISCP = "SPA";

/**
 * ISCP Command: SPA00
 * sets Speaker Off
 */
public static final String SPEAKER_A_OFF = "speaker-a::off";
// public static final String SPEAKER_A_OFF_ISCP = "SPA00";

/**
 * ISCP Command: SPA01
 * sets Speaker On
 */
public static final String SPEAKER_A_ON = "speaker-a::on";
// public static final String SPEAKER_A_ON_ISCP = "SPA01";

/**
 * ISCP Command: SPAQSTN
 * gets the Speaker State
 */
public static final String SPEAKER_A_QUERY = "speaker-a::query";
// public static final String SPEAKER_A_QUERY_ISCP = "SPAQSTN";

/**
 * ISCP Command: SPAUP
 * sets Speaker Switch Wrap-Around
 */
public static final String SPEAKER_A_UP = "speaker-a::up";
// public static final String SPEAKER_A_UP_ISCP = "SPAUP";

/**
 * CommandBlock
 * ISCP Command prefix: SPB
 * Speaker B Command
 */
public static final String SPEAKER_B = "speaker-b";
public static final String SPEAKER_B_ISCP = "SPB";

/**
 * ISCP Command: SPB00
 * sets Speaker Off
 */
public static final String SPEAKER_B_OFF = "speaker-b::off";
// public static final String SPEAKER_B_OFF_ISCP = "SPB00";

/**
 * ISCP Command: SPB01
 * sets Speaker On
 */
public static final String SPEAKER_B_ON = "speaker-b::on";
// public static final String SPEAKER_B_ON_ISCP = "SPB01";

/**
 * ISCP Command: SPBQSTN
 * gets the Speaker State
 */
public static final String SPEAKER_B_QUERY = "speaker-b::query";
// public static final String SPEAKER_B_QUERY_ISCP = "SPBQSTN";

/**
 * ISCP Command: SPBUP
 * sets Speaker Switch Wrap-Around
 */
public static final String SPEAKER_B_UP = "speaker-b::up";
// public static final String SPEAKER_B_UP_ISCP = "SPBUP";

/**
 * CommandBlock
 * ISCP Command prefix: SPL
 * Speaker Layout Command
 */
public static final String SPEAKER_LAYOUT = "speaker-layout";
public static final String SPEAKER_LAYOUT_ISCP = "SPL";

/**
 * ISCP Command: SPLFH
 * sets Front High Speaker / SurrBack+Front High Speakers
 */
public static final String SPEAKER_LAYOUT_FRONT_HIGH = "speaker-layout::front-high";
// public static final String SPEAKER_LAYOUT_FRONT_HIGH_ISCP = "SPLFH";

/**
 * ISCP Command: SPLHW
 * sets, Front High+Front Wide Speakers
 */
public static final String SPEAKER_LAYOUT_FRONT_HIGH_FRONT_WIDE_SPEAKERS = "speaker-layout::front-high-front-wide-speakers";
// public static final String SPEAKER_LAYOUT_FRONT_HIGH_FRONT_WIDE_SPEAKERS_ISCP = "SPLHW";

/**
 * ISCP Command: SPLFW
 * sets Front Wide Speaker / SurrBack+Front Wide Speakers
 */
public static final String SPEAKER_LAYOUT_FRONT_WIDE = "speaker-layout::front-wide";
// public static final String SPEAKER_LAYOUT_FRONT_WIDE_ISCP = "SPLFW";

/**
 * ISCP Command: SPLQSTN
 * gets the Speaker State
 */
public static final String SPEAKER_LAYOUT_QUERY = "speaker-layout::query";
// public static final String SPEAKER_LAYOUT_QUERY_ISCP = "SPLQSTN";

/**
 * ISCP Command: SPLSB
 * sets SurrBack Speaker
 */
public static final String SPEAKER_LAYOUT_SURRBACK = "speaker-layout::surrback";
// public static final String SPEAKER_LAYOUT_SURRBACK_ISCP = "SPLSB";

/**
 * ISCP Command: SPLUP
 * sets Speaker Switch Wrap-Around
 */
public static final String SPEAKER_LAYOUT_UP = "speaker-layout::up";
// public static final String SPEAKER_LAYOUT_UP_ISCP = "SPLUP";

/**
 * CommandBlock
 * ISCP Command prefix: SLC
 * Speaker Level Calibration Command
 */
public static final String SPEAKER_LEVEL_CALIBRATION = "speaker-level-calibration";
public static final String SPEAKER_LEVEL_CALIBRATION_ISCP = "SLC";

/**
 * ISCP Command: SLCCHSEL
 * CH SEL Key
 */
public static final String SPEAKER_LEVEL_CALIBRATION_CHSEL = "speaker-level-calibration::chsel";
// public static final String SPEAKER_LEVEL_CALIBRATION_CHSEL_ISCP = "SLCCHSEL";

/**
 * ISCP Command: SLCDOWN
 * LEVEL ? KEY
 */
public static final String SPEAKER_LEVEL_CALIBRATION_DOWN = "speaker-level-calibration::down";
// public static final String SPEAKER_LEVEL_CALIBRATION_DOWN_ISCP = "SLCDOWN";

/**
 * ISCP Command: SLCTEST
 * TEST Key
 */
public static final String SPEAKER_LEVEL_CALIBRATION_TEST = "speaker-level-calibration::test";
// public static final String SPEAKER_LEVEL_CALIBRATION_TEST_ISCP = "SLCTEST";

/**
 * ISCP Command: SLCUP
 * LEVEL + Key
 */
public static final String SPEAKER_LEVEL_CALIBRATION_UP = "speaker-level-calibration::up";
// public static final String SPEAKER_LEVEL_CALIBRATION_UP_ISCP = "SLCUP";

/**
 * CommandBlock
 * ISCP Command prefix: SWL
 * Subwoofer (temporary) Level Command
 */
public static final String SUBWOOFER_TEMPORARY_LEVEL = "subwoofer-temporary-level";
public static final String SUBWOOFER_TEMPORARY_LEVEL_ISCP = "SWL";

/**
 * ISCP Command: SWLDOWN
 * LEVEL ? KEY
 */
public static final String SUBWOOFER_TEMPORARY_LEVEL_DOWN = "subwoofer-temporary-level::down";
// public static final String SUBWOOFER_TEMPORARY_LEVEL_DOWN_ISCP = "SWLDOWN";

/**
 * ISCP Command: SWLQSTN
 * gets the Subwoofer Level
 */
public static final String SUBWOOFER_TEMPORARY_LEVEL_QUERY = "subwoofer-temporary-level::query";
// public static final String SUBWOOFER_TEMPORARY_LEVEL_QUERY_ISCP = "SWLQSTN";

/**
 * ISCP Command: SWLUP
 * LEVEL + Key
 */
public static final String SUBWOOFER_TEMPORARY_LEVEL_UP = "subwoofer-temporary-level::up";
// public static final String SUBWOOFER_TEMPORARY_LEVEL_UP_ISCP = "SWLUP";

/**
 * CommandBlock
 * ISCP Command prefix: PWR
 * System Power Command
 */
public static final String SYSTEM_POWER = "system-power";
public static final String SYSTEM_POWER_ISCP = "PWR";

/**
 * ISCP Command: PWR01
 * sets System On
 */
public static final String SYSTEM_POWER_ON = "system-power::on";
// public static final String SYSTEM_POWER_ON_ISCP = "PWR01";

/**
 * ISCP Command: PWRQSTN
 * gets the System Power Status
 */
public static final String SYSTEM_POWER_QUERY = "system-power::query";
// public static final String SYSTEM_POWER_QUERY_ISCP = "PWRQSTN";

/**
 * ISCP Command: PWR00
 * sets System Standby
 */
public static final String SYSTEM_POWER_STANDBY = "system-power::standby";
// public static final String SYSTEM_POWER_STANDBY_ISCP = "PWR00";

/**
 * CommandBlock
 * ISCP Command prefix: CT1
 * TAPE1(A) Operation Command
 */
public static final String TAPE1_A = "tape1-a";
public static final String TAPE1_A_ISCP = "CT1";

/**
 * ISCP Command: CT1FF
 * FF >>
 */
public static final String TAPE1_A_FF = "tape1-a::ff";
// public static final String TAPE1_A_FF_ISCP = "CT1FF";

/**
 * ISCP Command: CT1PLAY.F
 * PLAY >
 */
public static final String TAPE1_A_PLAY_F = "tape1-a::play-f";
// public static final String TAPE1_A_PLAY_F_ISCP = "CT1PLAY.F";

/**
 * ISCP Command: CT1PLAY.R
 * PLAY <
 */
public static final String TAPE1_A_PLAY_R = "tape1-a::play-r";
// public static final String TAPE1_A_PLAY_R_ISCP = "CT1PLAY.R";

/**
 * ISCP Command: CT1RC/PAU
 * REC/PAUSE
 */
public static final String TAPE1_A_RC_PAU = "tape1-a::rc-pau";
// public static final String TAPE1_A_RC_PAU_ISCP = "CT1RC/PAU";

/**
 * ISCP Command: CT1REW
 * REW <<
 */
public static final String TAPE1_A_REW = "tape1-a::rew";
// public static final String TAPE1_A_REW_ISCP = "CT1REW";

/**
 * ISCP Command: CT1STOP
 * STOP
 */
public static final String TAPE1_A_STOP = "tape1-a::stop";
// public static final String TAPE1_A_STOP_ISCP = "CT1STOP";

/**
 * CommandBlock
 * ISCP Command prefix: CT2
 * TAPE2(B) Operation Command
 */
public static final String TAPE2_B = "tape2-b";
public static final String TAPE2_B_ISCP = "CT2";

/**
 * ISCP Command: CT2FF
 * FF >>
 */
public static final String TAPE2_B_FF = "tape2-b::ff";
// public static final String TAPE2_B_FF_ISCP = "CT2FF";

/**
 * ISCP Command: CT2OP/CL
 * OPEN/CLOSE
 */
public static final String TAPE2_B_OP_CL = "tape2-b::op-cl";
// public static final String TAPE2_B_OP_CL_ISCP = "CT2OP/CL";

/**
 * ISCP Command: CT2PLAY.F
 * PLAY >
 */
public static final String TAPE2_B_PLAY_F = "tape2-b::play-f";
// public static final String TAPE2_B_PLAY_F_ISCP = "CT2PLAY.F";

/**
 * ISCP Command: CT2PLAY.R
 * PLAY <
 */
public static final String TAPE2_B_PLAY_R = "tape2-b::play-r";
// public static final String TAPE2_B_PLAY_R_ISCP = "CT2PLAY.R";

/**
 * ISCP Command: CT2RC/PAU
 * REC/PAUSE
 */
public static final String TAPE2_B_RC_PAU = "tape2-b::rc-pau";
// public static final String TAPE2_B_RC_PAU_ISCP = "CT2RC/PAU";

/**
 * ISCP Command: CT2REC
 * REC
 */
public static final String TAPE2_B_REC = "tape2-b::rec";
// public static final String TAPE2_B_REC_ISCP = "CT2REC";

/**
 * ISCP Command: CT2REW
 * REW <<
 */
public static final String TAPE2_B_REW = "tape2-b::rew";
// public static final String TAPE2_B_REW_ISCP = "CT2REW";

/**
 * ISCP Command: CT2SKIP.F
 * >>I
 */
public static final String TAPE2_B_SKIP_F = "tape2-b::skip-f";
// public static final String TAPE2_B_SKIP_F_ISCP = "CT2SKIP.F";

/**
 * ISCP Command: CT2SKIP.R
 * I<<
 */
public static final String TAPE2_B_SKIP_R = "tape2-b::skip-r";
// public static final String TAPE2_B_SKIP_R_ISCP = "CT2SKIP.R";

/**
 * ISCP Command: CT2STOP
 * STOP
 */
public static final String TAPE2_B_STOP = "tape2-b::stop";
// public static final String TAPE2_B_STOP_ISCP = "CT2STOP";

/**
 * CommandBlock
 * ISCP Command prefix: TCT
 * Tone(Center) Command
 */
public static final String TONE_CENTER = "tone-center";
public static final String TONE_CENTER_ISCP = "TCT";

/**
 * ISCP Command: TCTB{xx}
 * Center Bass (xx is "-A"..."00"..."+A"[-10...0...+10 2 step]
 */
public static final String TONE_CENTER_B_XX = "tone-center::b-xx";
// public static final String TONE_CENTER_B_XX_ISCP = "TCTB{xx}";

/**
 * ISCP Command: TCTBDOWN
 * sets Center Bass down(2 step)
 */
public static final String TONE_CENTER_BASS_DOWN = "tone-center::bass-down";
// public static final String TONE_CENTER_BASS_DOWN_ISCP = "TCTBDOWN";

/**
 * ISCP Command: TCTBUP
 * sets Center Bass up(2 step)
 */
public static final String TONE_CENTER_BASS_UP = "tone-center::bass-up";
// public static final String TONE_CENTER_BASS_UP_ISCP = "TCTBUP";

/**
 * ISCP Command: TCTQSTN
 * gets Cetner Tone ("BxxTxx")
 */
public static final String TONE_CENTER_QUERY = "tone-center::query";
// public static final String TONE_CENTER_QUERY_ISCP = "TCTQSTN";

/**
 * ISCP Command: TCTT{xx}
 * Center Treble (xx is "-A"..."00"..."+A"[-10...0...+10 2 step]
 */
public static final String TONE_CENTER_T_XX = "tone-center::t-xx";
// public static final String TONE_CENTER_T_XX_ISCP = "TCTT{xx}";

/**
 * ISCP Command: TCTTDOWN
 * sets Center Treble down(2 step)
 */
public static final String TONE_CENTER_TREBLE_DOWN = "tone-center::treble-down";
// public static final String TONE_CENTER_TREBLE_DOWN_ISCP = "TCTTDOWN";

/**
 * ISCP Command: TCTTUP
 * sets Center Treble up(2 step)
 */
public static final String TONE_CENTER_TREBLE_UP = "tone-center::treble-up";
// public static final String TONE_CENTER_TREBLE_UP_ISCP = "TCTTUP";

/**
 * CommandBlock
 * ISCP Command prefix: TFR
 * Tone(Front) Command
 */
public static final String TONE_FRONT = "tone-front";
public static final String TONE_FRONT_ISCP = "TFR";

/**
 * ISCP Command: TFRB{xx}
 * Front Bass (xx is "-A"..."00"..."+A"[-10...0...+10 2 step]
 */
public static final String TONE_FRONT_B_XX = "tone-front::b-xx";
// public static final String TONE_FRONT_B_XX_ISCP = "TFRB{xx}";

/**
 * ISCP Command: TFRBDOWN
 * sets Front Bass down(2 step)
 */
public static final String TONE_FRONT_BASS_DOWN = "tone-front::bass-down";
// public static final String TONE_FRONT_BASS_DOWN_ISCP = "TFRBDOWN";

/**
 * ISCP Command: TFRBUP
 * sets Front Bass up(2 step)
 */
public static final String TONE_FRONT_BASS_UP = "tone-front::bass-up";
// public static final String TONE_FRONT_BASS_UP_ISCP = "TFRBUP";

/**
 * ISCP Command: TFRQSTN
 * gets Front Tone ("BxxTxx")
 */
public static final String TONE_FRONT_QUERY = "tone-front::query";
// public static final String TONE_FRONT_QUERY_ISCP = "TFRQSTN";

/**
 * ISCP Command: TFRT{xx}
 * Front Treble (xx is "-A"..."00"..."+A"[-10...0...+10 2 step]
 */
public static final String TONE_FRONT_T_XX = "tone-front::t-xx";
// public static final String TONE_FRONT_T_XX_ISCP = "TFRT{xx}";

/**
 * ISCP Command: TFRTDOWN
 * sets Front Treble down(2 step)
 */
public static final String TONE_FRONT_TREBLE_DOWN = "tone-front::treble-down";
// public static final String TONE_FRONT_TREBLE_DOWN_ISCP = "TFRTDOWN";

/**
 * ISCP Command: TFRTUP
 * sets Front Treble up(2 step)
 */
public static final String TONE_FRONT_TREBLE_UP = "tone-front::treble-up";
// public static final String TONE_FRONT_TREBLE_UP_ISCP = "TFRTUP";

/**
 * CommandBlock
 * ISCP Command prefix: TFH
 * Tone(Front High) Command
 */
public static final String TONE_FRONT_HIGH = "tone-front-high";
public static final String TONE_FRONT_HIGH_ISCP = "TFH";

/**
 * ISCP Command: TFHB{xx}
 * Front High Bass (xx is "-A"..."00"..."+A"[-10...0...+10 2 step]
 */
public static final String TONE_FRONT_HIGH_B_XX = "tone-front-high::b-xx";
// public static final String TONE_FRONT_HIGH_B_XX_ISCP = "TFHB{xx}";

/**
 * ISCP Command: TFHBDOWN
 * sets Front High Bass down(2 step)
 */
public static final String TONE_FRONT_HIGH_BASS_DOWN = "tone-front-high::bass-down";
// public static final String TONE_FRONT_HIGH_BASS_DOWN_ISCP = "TFHBDOWN";

/**
 * ISCP Command: TFHBUP
 * sets Front High Bass up(2 step)
 */
public static final String TONE_FRONT_HIGH_BASS_UP = "tone-front-high::bass-up";
// public static final String TONE_FRONT_HIGH_BASS_UP_ISCP = "TFHBUP";

/**
 * ISCP Command: TFHQSTN
 * gets Front High Tone ("BxxTxx")
 */
public static final String TONE_FRONT_HIGH_QUERY = "tone-front-high::query";
// public static final String TONE_FRONT_HIGH_QUERY_ISCP = "TFHQSTN";

/**
 * ISCP Command: TFHT{xx}
 * Front High Treble (xx is "-A"..."00"..."+A"[-10...0...+10 2 step]
 */
public static final String TONE_FRONT_HIGH_T_XX = "tone-front-high::t-xx";
// public static final String TONE_FRONT_HIGH_T_XX_ISCP = "TFHT{xx}";

/**
 * ISCP Command: TFHTDOWN
 * sets Front High Treble down(2 step)
 */
public static final String TONE_FRONT_HIGH_TREBLE_DOWN = "tone-front-high::treble-down";
// public static final String TONE_FRONT_HIGH_TREBLE_DOWN_ISCP = "TFHTDOWN";

/**
 * ISCP Command: TFHTUP
 * sets Front High Treble up(2 step)
 */
public static final String TONE_FRONT_HIGH_TREBLE_UP = "tone-front-high::treble-up";
// public static final String TONE_FRONT_HIGH_TREBLE_UP_ISCP = "TFHTUP";

/**
 * CommandBlock
 * ISCP Command prefix: TFW
 * Tone(Front Wide) Command
 */
public static final String TONE_FRONT_WIDE = "tone-front-wide";
public static final String TONE_FRONT_WIDE_ISCP = "TFW";

/**
 * ISCP Command: TFWB{xx}
 * Front Wide Bass (xx is "-A"..."00"..."+A"[-10...0...+10 2 step]
 */
public static final String TONE_FRONT_WIDE_B_XX = "tone-front-wide::b-xx";
// public static final String TONE_FRONT_WIDE_B_XX_ISCP = "TFWB{xx}";

/**
 * ISCP Command: TFWBDOWN
 * sets Front Wide Bass down(2 step)
 */
public static final String TONE_FRONT_WIDE_BASS_DOWN = "tone-front-wide::bass-down";
// public static final String TONE_FRONT_WIDE_BASS_DOWN_ISCP = "TFWBDOWN";

/**
 * ISCP Command: TFWBUP
 * sets Front Wide Bass up(2 step)
 */
public static final String TONE_FRONT_WIDE_BASS_UP = "tone-front-wide::bass-up";
// public static final String TONE_FRONT_WIDE_BASS_UP_ISCP = "TFWBUP";

/**
 * ISCP Command: TFWQSTN
 * gets Front Wide Tone ("BxxTxx")
 */
public static final String TONE_FRONT_WIDE_QUERY = "tone-front-wide::query";
// public static final String TONE_FRONT_WIDE_QUERY_ISCP = "TFWQSTN";

/**
 * ISCP Command: TFWT{xx}
 * Front Wide Treble (xx is "-A"..."00"..."+A"[-10...0...+10 2 step]
 */
public static final String TONE_FRONT_WIDE_T_XX = "tone-front-wide::t-xx";
// public static final String TONE_FRONT_WIDE_T_XX_ISCP = "TFWT{xx}";

/**
 * ISCP Command: TFWTDOWN
 * sets Front Wide Treble down(2 step)
 */
public static final String TONE_FRONT_WIDE_TREBLE_DOWN = "tone-front-wide::treble-down";
// public static final String TONE_FRONT_WIDE_TREBLE_DOWN_ISCP = "TFWTDOWN";

/**
 * ISCP Command: TFWTUP
 * sets Front Wide Treble up(2 step)
 */
public static final String TONE_FRONT_WIDE_TREBLE_UP = "tone-front-wide::treble-up";
// public static final String TONE_FRONT_WIDE_TREBLE_UP_ISCP = "TFWTUP";

/**
 * CommandBlock
 * ISCP Command prefix: TSW
 * Tone(Subwoofer) Command
 */
public static final String TONE_SUBWOOFER = "tone-subwoofer";
public static final String TONE_SUBWOOFER_ISCP = "TSW";

/**
 * ISCP Command: TSWB{xx}
 * Subwoofer Bass (xx is "-A"..."00"..."+A"[-10...0...+10 2 step]
 */
public static final String TONE_SUBWOOFER_B_XX = "tone-subwoofer::b-xx";
// public static final String TONE_SUBWOOFER_B_XX_ISCP = "TSWB{xx}";

/**
 * ISCP Command: TSWBDOWN
 * sets Subwoofer Bass down(2 step)
 */
public static final String TONE_SUBWOOFER_BASS_DOWN = "tone-subwoofer::bass-down";
// public static final String TONE_SUBWOOFER_BASS_DOWN_ISCP = "TSWBDOWN";

/**
 * ISCP Command: TSWBUP
 * sets Subwoofer Bass up(2 step)
 */
public static final String TONE_SUBWOOFER_BASS_UP = "tone-subwoofer::bass-up";
// public static final String TONE_SUBWOOFER_BASS_UP_ISCP = "TSWBUP";

/**
 * ISCP Command: TSWQSTN
 * gets Subwoofer Tone ("BxxTxx")
 */
public static final String TONE_SUBWOOFER_QUERY = "tone-subwoofer::query";
// public static final String TONE_SUBWOOFER_QUERY_ISCP = "TSWQSTN";

/**
 * CommandBlock
 * ISCP Command prefix: TSR
 * Tone(Surround) Command
 */
public static final String TONE_SURROUND = "tone-surround";
public static final String TONE_SURROUND_ISCP = "TSR";

/**
 * ISCP Command: TSRB{xx}
 * Surround Bass (xx is "-A"..."00"..."+A"[-10...0...+10 2 step]
 */
public static final String TONE_SURROUND_B_XX = "tone-surround::b-xx";
// public static final String TONE_SURROUND_B_XX_ISCP = "TSRB{xx}";

/**
 * ISCP Command: TSRBDOWN
 * sets Surround Bass down(2 step)
 */
public static final String TONE_SURROUND_BASS_DOWN = "tone-surround::bass-down";
// public static final String TONE_SURROUND_BASS_DOWN_ISCP = "TSRBDOWN";

/**
 * ISCP Command: TSRBUP
 * sets Surround Bass up(2 step)
 */
public static final String TONE_SURROUND_BASS_UP = "tone-surround::bass-up";
// public static final String TONE_SURROUND_BASS_UP_ISCP = "TSRBUP";

/**
 * ISCP Command: TSRQSTN
 * gets Surround Tone ("BxxTxx")
 */
public static final String TONE_SURROUND_QUERY = "tone-surround::query";
// public static final String TONE_SURROUND_QUERY_ISCP = "TSRQSTN";

/**
 * ISCP Command: TSRT{xx}
 * Surround Treble (xx is "-A"..."00"..."+A"[-10...0...+10 2 step]
 */
public static final String TONE_SURROUND_T_XX = "tone-surround::t-xx";
// public static final String TONE_SURROUND_T_XX_ISCP = "TSRT{xx}";

/**
 * ISCP Command: TSRTDOWN
 * sets Surround Treble down(2 step)
 */
public static final String TONE_SURROUND_TREBLE_DOWN = "tone-surround::treble-down";
// public static final String TONE_SURROUND_TREBLE_DOWN_ISCP = "TSRTDOWN";

/**
 * ISCP Command: TSRTUP
 * sets Surround Treble up(2 step)
 */
public static final String TONE_SURROUND_TREBLE_UP = "tone-surround::treble-up";
// public static final String TONE_SURROUND_TREBLE_UP_ISCP = "TSRTUP";

/**
 * CommandBlock
 * ISCP Command prefix: TSB
 * Tone(Surround Back) Command
 */
public static final String TONE_SURROUND_BACK = "tone-surround-back";
public static final String TONE_SURROUND_BACK_ISCP = "TSB";

/**
 * ISCP Command: TSBB{xx}
 * Surround Back Bass (xx is "-A"..."00"..."+A"[-10...0...+10 2 step]
 */
public static final String TONE_SURROUND_BACK_B_XX = "tone-surround-back::b-xx";
// public static final String TONE_SURROUND_BACK_B_XX_ISCP = "TSBB{xx}";

/**
 * ISCP Command: TSBBDOWN
 * sets Surround Back Bass down(2 step)
 */
public static final String TONE_SURROUND_BACK_BASS_DOWN = "tone-surround-back::bass-down";
// public static final String TONE_SURROUND_BACK_BASS_DOWN_ISCP = "TSBBDOWN";

/**
 * ISCP Command: TSBBUP
 * sets Surround Back Bass up(2 step)
 */
public static final String TONE_SURROUND_BACK_BASS_UP = "tone-surround-back::bass-up";
// public static final String TONE_SURROUND_BACK_BASS_UP_ISCP = "TSBBUP";

/**
 * ISCP Command: TSBQSTN
 * gets Surround Back Tone ("BxxTxx")
 */
public static final String TONE_SURROUND_BACK_QUERY = "tone-surround-back::query";
// public static final String TONE_SURROUND_BACK_QUERY_ISCP = "TSBQSTN";

/**
 * ISCP Command: TSBT{xx}
 * Surround Back Treble (xx is "-A"..."00"..."+A"[-10...0...+10 2 step]
 */
public static final String TONE_SURROUND_BACK_T_XX = "tone-surround-back::t-xx";
// public static final String TONE_SURROUND_BACK_T_XX_ISCP = "TSBT{xx}";

/**
 * ISCP Command: TSBTDOWN
 * sets Surround Back Treble down(2 step)
 */
public static final String TONE_SURROUND_BACK_TREBLE_DOWN = "tone-surround-back::treble-down";
// public static final String TONE_SURROUND_BACK_TREBLE_DOWN_ISCP = "TSBTDOWN";

/**
 * ISCP Command: TSBTUP
 * sets Surround Back Treble up(2 step)
 */
public static final String TONE_SURROUND_BACK_TREBLE_UP = "tone-surround-back::treble-up";
// public static final String TONE_SURROUND_BACK_TREBLE_UP_ISCP = "TSBTUP";

/**
 * CommandBlock
 * ISCP Command prefix: TPS
 * TP Scan Command (RDS Model Only)
 */
public static final String TP_SCAN = "tp-scan";
public static final String TP_SCAN_ISCP = "TPS";

/**
 * ISCP Command: TPSENTER
 * Finish TP Scan
 */
public static final String TP_SCAN_ENTER = "tp-scan::enter";
// public static final String TP_SCAN_ENTER_ISCP = "TPSENTER";

/**
 * ISCP Command: TPS
 * Start TP Scan (When Don?t Have Parameter)
 */
public static final String TP_SCAN_NULL = "tp-scan::null";
// public static final String TP_SCAN_NULL_ISCP = "TPS";

/**
 * CommandBlock
 * ISCP Command prefix: UTN
 * Tuning Command (Universal Port Dock Only)
 */
public static final String TUNING = "tuning";
public static final String TUNING_ISCP = "UTN";

/**
 * ISCP Command: UTNDOWN
 * sets Tuning Frequency Wrap-Around Down
 */
public static final String TUNING_DOWN = "tuning::down";
// public static final String TUNING_DOWN_ISCP = "UTNDOWN";

/**
 * ISCP Command: UTNnnnnn
 * sets Directly Tuning Frequency (FM nnn.nn MHz / AM nnnnn kHz)
 */
public static final String TUNING_NULL = "tuning::null";
// public static final String TUNING_NULL_ISCP = "UTNnnnnn";

/**
 * ISCP Command: UTNQSTN
 * gets The Tuning Frequency
 */
public static final String TUNING_QUERY = "tuning::query";
// public static final String TUNING_QUERY_ISCP = "UTNQSTN";

/**
 * ISCP Command: UTNUP
 * sets Tuning Frequency Wrap-Around Up
 */
public static final String TUNING_UP = "tuning::up";
// public static final String TUNING_UP_ISCP = "UTNUP";

/**
 * CommandBlock
 * ISCP Command prefix: CPT
 * Universal PORT Operation Command
 */
public static final String UNIVERSAL_PORT = "universal-port";
public static final String UNIVERSAL_PORT_ISCP = "CPT";

/**
 * ISCP Command: CPT0
 * 0.0
 */
public static final String UNIVERSAL_PORT_0 = "universal-port::0";
// public static final String UNIVERSAL_PORT_0_ISCP = "CPT0";

/**
 * ISCP Command: CPT1
 * 1.0
 */
public static final String UNIVERSAL_PORT_1 = "universal-port::1";
// public static final String UNIVERSAL_PORT_1_ISCP = "CPT1";

/**
 * ISCP Command: CPT10
 * 10/+10/Direct Tuning
 */
public static final String UNIVERSAL_PORT_10 = "universal-port::10";
// public static final String UNIVERSAL_PORT_10_ISCP = "CPT10";

/**
 * ISCP Command: CPT2
 * 2.0
 */
public static final String UNIVERSAL_PORT_2 = "universal-port::2";
// public static final String UNIVERSAL_PORT_2_ISCP = "CPT2";

/**
 * ISCP Command: CPT3
 * 3.0
 */
public static final String UNIVERSAL_PORT_3 = "universal-port::3";
// public static final String UNIVERSAL_PORT_3_ISCP = "CPT3";

/**
 * ISCP Command: CPT4
 * 4.0
 */
public static final String UNIVERSAL_PORT_4 = "universal-port::4";
// public static final String UNIVERSAL_PORT_4_ISCP = "CPT4";

/**
 * ISCP Command: CPT5
 * 5.0
 */
public static final String UNIVERSAL_PORT_5 = "universal-port::5";
// public static final String UNIVERSAL_PORT_5_ISCP = "CPT5";

/**
 * ISCP Command: CPT6
 * 6.0
 */
public static final String UNIVERSAL_PORT_6 = "universal-port::6";
// public static final String UNIVERSAL_PORT_6_ISCP = "CPT6";

/**
 * ISCP Command: CPT7
 * 7.0
 */
public static final String UNIVERSAL_PORT_7 = "universal-port::7";
// public static final String UNIVERSAL_PORT_7_ISCP = "CPT7";

/**
 * ISCP Command: CPT8
 * 8.0
 */
public static final String UNIVERSAL_PORT_8 = "universal-port::8";
// public static final String UNIVERSAL_PORT_8_ISCP = "CPT8";

/**
 * ISCP Command: CPT9
 * 9.0
 */
public static final String UNIVERSAL_PORT_9 = "universal-port::9";
// public static final String UNIVERSAL_PORT_9_ISCP = "CPT9";

/**
 * ISCP Command: CPTDISP
 * DISPLAY
 */
public static final String UNIVERSAL_PORT_DISP = "universal-port::disp";
// public static final String UNIVERSAL_PORT_DISP_ISCP = "CPTDISP";

/**
 * ISCP Command: CPTDOWN
 * DOWN/Tuning Down
 */
public static final String UNIVERSAL_PORT_DOWN = "universal-port::down";
// public static final String UNIVERSAL_PORT_DOWN_ISCP = "CPTDOWN";

/**
 * ISCP Command: CPTENTER
 * ENTER
 */
public static final String UNIVERSAL_PORT_ENTER = "universal-port::enter";
// public static final String UNIVERSAL_PORT_ENTER_ISCP = "CPTENTER";

/**
 * ISCP Command: CPTFF
 * FF >>
 */
public static final String UNIVERSAL_PORT_FF = "universal-port::ff";
// public static final String UNIVERSAL_PORT_FF_ISCP = "CPTFF";

/**
 * ISCP Command: CPTLEFT
 * LEFT/Multicast Down
 */
public static final String UNIVERSAL_PORT_LEFT = "universal-port::left";
// public static final String UNIVERSAL_PORT_LEFT_ISCP = "CPTLEFT";

/**
 * ISCP Command: CPTMODE
 * MODE
 */
public static final String UNIVERSAL_PORT_MODE = "universal-port::mode";
// public static final String UNIVERSAL_PORT_MODE_ISCP = "CPTMODE";

/**
 * ISCP Command: CPTPAUSE
 * PAUSE
 */
public static final String UNIVERSAL_PORT_PAUSE = "universal-port::pause";
// public static final String UNIVERSAL_PORT_PAUSE_ISCP = "CPTPAUSE";

/**
 * ISCP Command: CPTPLAY
 * PLAY/BAND
 */
public static final String UNIVERSAL_PORT_PLAY = "universal-port::play";
// public static final String UNIVERSAL_PORT_PLAY_ISCP = "CPTPLAY";

/**
 * ISCP Command: CPTPRSDN
 * PRESET DOWN
 */
public static final String UNIVERSAL_PORT_PRSDN = "universal-port::prsdn";
// public static final String UNIVERSAL_PORT_PRSDN_ISCP = "CPTPRSDN";

/**
 * ISCP Command: CPTPRSUP
 * PRESET UP
 */
public static final String UNIVERSAL_PORT_PRSUP = "universal-port::prsup";
// public static final String UNIVERSAL_PORT_PRSUP_ISCP = "CPTPRSUP";

/**
 * ISCP Command: CPTREPEAT
 * REPEAT
 */
public static final String UNIVERSAL_PORT_REPEAT = "universal-port::repeat";
// public static final String UNIVERSAL_PORT_REPEAT_ISCP = "CPTREPEAT";

/**
 * ISCP Command: CPTRETURN
 * RETURN
 */
public static final String UNIVERSAL_PORT_RETURN = "universal-port::return";
// public static final String UNIVERSAL_PORT_RETURN_ISCP = "CPTRETURN";

/**
 * ISCP Command: CPTREW
 * REW <<
 */
public static final String UNIVERSAL_PORT_REW = "universal-port::rew";
// public static final String UNIVERSAL_PORT_REW_ISCP = "CPTREW";

/**
 * ISCP Command: CPTRIGHT
 * RIGHT/Multicast Up
 */
public static final String UNIVERSAL_PORT_RIGHT = "universal-port::right";
// public static final String UNIVERSAL_PORT_RIGHT_ISCP = "CPTRIGHT";

/**
 * ISCP Command: CPTSETUP
 * SETUP
 */
public static final String UNIVERSAL_PORT_SETUP = "universal-port::setup";
// public static final String UNIVERSAL_PORT_SETUP_ISCP = "CPTSETUP";

/**
 * ISCP Command: CPTSHUFFLE
 * SHUFFLE
 */
public static final String UNIVERSAL_PORT_SHUFFLE = "universal-port::shuffle";
// public static final String UNIVERSAL_PORT_SHUFFLE_ISCP = "CPTSHUFFLE";

/**
 * ISCP Command: CPTSKIP.F
 * >>I
 */
public static final String UNIVERSAL_PORT_SKIP_F = "universal-port::skip-f";
// public static final String UNIVERSAL_PORT_SKIP_F_ISCP = "CPTSKIP.F";

/**
 * ISCP Command: CPTSKIP.R
 * I<<
 */
public static final String UNIVERSAL_PORT_SKIP_R = "universal-port::skip-r";
// public static final String UNIVERSAL_PORT_SKIP_R_ISCP = "CPTSKIP.R";

/**
 * ISCP Command: CPTSTOP
 * STOP
 */
public static final String UNIVERSAL_PORT_STOP = "universal-port::stop";
// public static final String UNIVERSAL_PORT_STOP_ISCP = "CPTSTOP";

/**
 * ISCP Command: CPTUP
 * UP/Tuning Up
 */
public static final String UNIVERSAL_PORT_UP = "universal-port::up";
// public static final String UNIVERSAL_PORT_UP_ISCP = "CPTUP";

/**
 * CommandBlock
 * ISCP Command prefix: IFV
 * Video Infomation Command
 */
public static final String VIDEO_INFOMATION = "video-infomation";
public static final String VIDEO_INFOMATION_ISCP = "IFV";

/**
 * ISCP Command: IFVnnnnn:nnnnn
 * infomation of Video(Same Immediate Display ',' is separator of infomations)
 */
public static final String VIDEO_INFOMATION_NULL = "video-infomation::null";
// public static final String VIDEO_INFOMATION_NULL_ISCP = "IFVnnnnn:nnnnn";

/**
 * ISCP Command: IFVQSTN
 * gets Infomation of Video
 */
public static final String VIDEO_INFOMATION_QUERY = "video-infomation::query";
// public static final String VIDEO_INFOMATION_QUERY_ISCP = "IFVQSTN";

/**
 * CommandBlock
 * ISCP Command prefix: VOS
 * Video Output Selector (Japanese Model Only)
 */
public static final String VIDEO_OUTPUT_SELECTOR = "video-output-selector";
public static final String VIDEO_OUTPUT_SELECTOR_ISCP = "VOS";

/**
 * ISCP Command: VOS01
 * sets Component
 */
public static final String VIDEO_OUTPUT_SELECTOR_COMPONENT = "video-output-selector::component";
// public static final String VIDEO_OUTPUT_SELECTOR_COMPONENT_ISCP = "VOS01";

/**
 * ISCP Command: VOS00
 * sets D4
 */
public static final String VIDEO_OUTPUT_SELECTOR_D4 = "video-output-selector::d4";
// public static final String VIDEO_OUTPUT_SELECTOR_D4_ISCP = "VOS00";

/**
 * ISCP Command: VOSQSTN
 * gets The Selector Position
 */
public static final String VIDEO_OUTPUT_SELECTOR_QUERY = "video-output-selector::query";
// public static final String VIDEO_OUTPUT_SELECTOR_QUERY_ISCP = "VOSQSTN";

/**
 * CommandBlock
 * ISCP Command prefix: VPM
 * Video Picture Mode
 */
public static final String VIDEO_PICTURE_MODE = "video-picture-mode";
public static final String VIDEO_PICTURE_MODE_ISCP = "VPM";

/**
 * ISCP Command: VPM02
 * sets Cinema
 */
public static final String VIDEO_PICTURE_MODE_CINEMA = "video-picture-mode::cinema";
// public static final String VIDEO_PICTURE_MODE_CINEMA_ISCP = "VPM02";

/**
 * ISCP Command: VPM01
 * sets Custom
 */
public static final String VIDEO_PICTURE_MODE_CUSTOM = "video-picture-mode::custom";
// public static final String VIDEO_PICTURE_MODE_CUSTOM_ISCP = "VPM01";

/**
 * ISCP Command: VPM03
 * sets Game
 */
public static final String VIDEO_PICTURE_MODE_GAME = "video-picture-mode::game";
// public static final String VIDEO_PICTURE_MODE_GAME_ISCP = "VPM03";

/**
 * ISCP Command: VPM05
 * sets ISF Day
 */
public static final String VIDEO_PICTURE_MODE_ISF_DAY = "video-picture-mode::isf-day";
// public static final String VIDEO_PICTURE_MODE_ISF_DAY_ISCP = "VPM05";

/**
 * ISCP Command: VPM06
 * sets ISF Night
 */
public static final String VIDEO_PICTURE_MODE_ISF_NIGHT = "video-picture-mode::isf-night";
// public static final String VIDEO_PICTURE_MODE_ISF_NIGHT_ISCP = "VPM06";

/**
 * ISCP Command: VPMQSTN
 * gets Video Zoom Mode
 */
public static final String VIDEO_PICTURE_MODE_QUERY = "video-picture-mode::query";
// public static final String VIDEO_PICTURE_MODE_QUERY_ISCP = "VPMQSTN";

/**
 * ISCP Command: VPM07
 * sets Streaming
 */
public static final String VIDEO_PICTURE_MODE_STREAMING = "video-picture-mode::streaming";
// public static final String VIDEO_PICTURE_MODE_STREAMING_ISCP = "VPM07";

/**
 * ISCP Command: VPM00
 * sets Through
 */
public static final String VIDEO_PICTURE_MODE_THROUGH = "video-picture-mode::through";
// public static final String VIDEO_PICTURE_MODE_THROUGH_ISCP = "VPM00";

/**
 * ISCP Command: VPMUP
 * sets Video Zoom Mode Wrap-Around Up
 */
public static final String VIDEO_PICTURE_MODE_UP = "video-picture-mode::up";
// public static final String VIDEO_PICTURE_MODE_UP_ISCP = "VPMUP";

/**
 * CommandBlock
 * ISCP Command prefix: VWM
 * Video Wide Mode
 */
public static final String VIDEO_WIDE_MODE = "video-wide-mode";
public static final String VIDEO_WIDE_MODE_ISCP = "VWM";

/**
 * ISCP Command: VWM01
 * sets 4:3
 */
public static final String VIDEO_WIDE_MODE_4_3 = "video-wide-mode::4-3";
// public static final String VIDEO_WIDE_MODE_4_3_ISCP = "VWM01";

/**
 * ISCP Command: VWM00
 * sets Auto
 */
public static final String VIDEO_WIDE_MODE_AUTO = "video-wide-mode::auto";
// public static final String VIDEO_WIDE_MODE_AUTO_ISCP = "VWM00";

/**
 * ISCP Command: VWM02
 * sets Full
 */
public static final String VIDEO_WIDE_MODE_FULL = "video-wide-mode::full";
// public static final String VIDEO_WIDE_MODE_FULL_ISCP = "VWM02";

/**
 * ISCP Command: VWMQSTN
 * gets Video Zoom Mode
 */
public static final String VIDEO_WIDE_MODE_QUERY = "video-wide-mode::query";
// public static final String VIDEO_WIDE_MODE_QUERY_ISCP = "VWMQSTN";

/**
 * ISCP Command: VWM05
 * sets Smart Zoom
 */
public static final String VIDEO_WIDE_MODE_SMART_ZOOM = "video-wide-mode::smart-zoom";
// public static final String VIDEO_WIDE_MODE_SMART_ZOOM_ISCP = "VWM05";

/**
 * ISCP Command: VWMUP
 * sets Video Zoom Mode Wrap-Around Up
 */
public static final String VIDEO_WIDE_MODE_UP = "video-wide-mode::up";
// public static final String VIDEO_WIDE_MODE_UP_ISCP = "VWMUP";

/**
 * ISCP Command: VWM04
 * sets Wide Zoom
 */
public static final String VIDEO_WIDE_MODE_ZOOM = "video-wide-mode::zoom";
// public static final String VIDEO_WIDE_MODE_ZOOM_ISCP = "VWM04";

/**
 * CommandBlock
 * ISCP Command prefix: XAT
 * XM Artist Name Info (XM Model Only)
 */
public static final String XM_ARTIST_NAME_INFO = "xm-artist-name-info";
public static final String XM_ARTIST_NAME_INFO_ISCP = "XAT";

/**
 * ISCP Command: XATnnnnnnnnnn
 * XM Artist Name
 */
public static final String XM_ARTIST_NAME_INFO_NULL = "xm-artist-name-info::null";
// public static final String XM_ARTIST_NAME_INFO_NULL_ISCP = "XATnnnnnnnnnn";

/**
 * ISCP Command: XATQSTN
 * gets XM Artist Name
 */
public static final String XM_ARTIST_NAME_INFO_QUERY = "xm-artist-name-info::query";
// public static final String XM_ARTIST_NAME_INFO_QUERY_ISCP = "XATQSTN";

/**
 * CommandBlock
 * ISCP Command prefix: XCT
 * XM Category Command (XM Model Only)
 */
public static final String XM_CATEGORY = "xm-category";
public static final String XM_CATEGORY_ISCP = "XCT";

/**
 * ISCP Command: XCTDOWN
 * sets XM Category Wrap-Around Down
 */
public static final String XM_CATEGORY_DOWN = "xm-category::down";
// public static final String XM_CATEGORY_DOWN_ISCP = "XCTDOWN";

/**
 * ISCP Command: XCTnnnnnnnnnn
 * XM Category Info
 */
public static final String XM_CATEGORY_NULL = "xm-category::null";
// public static final String XM_CATEGORY_NULL_ISCP = "XCTnnnnnnnnnn";

/**
 * ISCP Command: XCTQSTN
 * gets XM Category
 */
public static final String XM_CATEGORY_QUERY = "xm-category::query";
// public static final String XM_CATEGORY_QUERY_ISCP = "XCTQSTN";

/**
 * ISCP Command: XCTUP
 * sets XM Category Wrap-Around Up
 */
public static final String XM_CATEGORY_UP = "xm-category::up";
// public static final String XM_CATEGORY_UP_ISCP = "XCTUP";

/**
 * CommandBlock
 * ISCP Command prefix: XCN
 * XM Channel Name Info (XM Model Only)
 */
public static final String XM_CHANNEL_NAME_INFO = "xm-channel-name-info";
public static final String XM_CHANNEL_NAME_INFO_ISCP = "XCN";

/**
 * ISCP Command: XCNnnnnnnnnnn
 * XM Channel Name
 */
public static final String XM_CHANNEL_NAME_INFO_NULL = "xm-channel-name-info::null";
// public static final String XM_CHANNEL_NAME_INFO_NULL_ISCP = "XCNnnnnnnnnnn";

/**
 * ISCP Command: XCNQSTN
 * gets XM Channel Name
 */
public static final String XM_CHANNEL_NAME_INFO_QUERY = "xm-channel-name-info::query";
// public static final String XM_CHANNEL_NAME_INFO_QUERY_ISCP = "XCNQSTN";

/**
 * CommandBlock
 * ISCP Command prefix: XCH
 * XM Channel Number Command (XM Model Only)
 */
public static final String XM_CHANNEL_NUMBER = "xm-channel-number";
public static final String XM_CHANNEL_NUMBER_ISCP = "XCH";

/**
 * ISCP Command: XCHDOWN
 * sets XM Channel Wrap-Around Down
 */
public static final String XM_CHANNEL_NUMBER_DOWN = "xm-channel-number::down";
// public static final String XM_CHANNEL_NUMBER_DOWN_ISCP = "XCHDOWN";

/**
 * ISCP Command: XCHQSTN
 * gets XM Channel Number
 */
public static final String XM_CHANNEL_NUMBER_QUERY = "xm-channel-number::query";
// public static final String XM_CHANNEL_NUMBER_QUERY_ISCP = "XCHQSTN";

/**
 * ISCP Command: XCHUP
 * sets XM Channel Wrap-Around Up
 */
public static final String XM_CHANNEL_NUMBER_UP = "xm-channel-number::up";
// public static final String XM_CHANNEL_NUMBER_UP_ISCP = "XCHUP";

/**
 * CommandBlock
 * ISCP Command prefix: XTI
 * XM Title Info (XM Model Only)
 */
public static final String XM_TITLE_INFO = "xm-title-info";
public static final String XM_TITLE_INFO_ISCP = "XTI";

/**
 * ISCP Command: XTInnnnnnnnnn
 * XM Title
 */
public static final String XM_TITLE_INFO_NULL = "xm-title-info::null";
// public static final String XM_TITLE_INFO_NULL_ISCP = "XTInnnnnnnnnn";

/**
 * ISCP Command: XTIQSTN
 * gets XM Title
 */
public static final String XM_TITLE_INFO_QUERY = "xm-title-info::query";
// public static final String XM_TITLE_INFO_QUERY_ISCP = "XTIQSTN";

}