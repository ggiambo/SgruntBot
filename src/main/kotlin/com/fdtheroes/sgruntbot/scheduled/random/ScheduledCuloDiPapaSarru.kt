package com.fdtheroes.sgruntbot.scheduled.random

import com.fdtheroes.sgruntbot.models.ActionResponse
import com.fdtheroes.sgruntbot.scheduled.Scheduled
import com.fdtheroes.sgruntbot.utils.BotUtils
import com.fdtheroes.sgruntbot.utils.BotUtils.Companion.length
import org.springframework.stereotype.Service
import java.time.*

@Service
class ScheduledCuloDiPapaSarru(private val botUtils: BotUtils) : Scheduled {

    private val primoDiNovembre = LocalDate.of(2024, Month.NOVEMBER, 1)
    private val dieciDiMattina = LocalTime.of(10, 0)

    private val vangeloDellInfanziaSecondoSarrusofono = listOf(
        "Dhnyr vzcerpnmvbar cbgeò znv znaqnegv nssvapué vy Fvtaber pur un puvhfb vy ghb irager, aba gv qvn sehggb va Vfenryr?\n(Cebgbinatryb qv Tvnpbzb)",
        "Fvtaber Qvb qrv zvrv cnqev, nfpbygn yn zvn certuvren r orarqvpvzv pbzr unv orarqrggb yn ihyin qv Fnen qnaqbyr vy svtyvb Vfnppb. Thneqn pbfì yn ghn napryyn!\n(Pbqvpr qv Nehaqry 404)",
        "Nyyben yrv evfcbfr, zbezbenaqb: \"Fr Qvb gv un puvhfb y'hgreb r un fbggenggb qn gr vy ghb znevgb, vb pur gv cbffb sner?\". Nyy'hqver pvò, Naan cvnatrin napben qv cvù\n(Pbqvpr qv Nehaqry 404)",
        "Qvfpraqv qhadhr qnv zbagv r evgbean qn ghn zbtyvr; yn gebirenv vapvagn. Qvb, vasnggv, un evfirtyvngb vy frzr va yrv r y'un snggn znqer qv han orarqvmvbar rgrean.\n(Pbqvpr qv Nehaqry 404)",
        "Ary abab zrfr Naan cnegbeì r qbznaqò nyyn yringevpr: \"Pur pbfn ub cnegbevgb?\".\n(Cebgbinatryb qv Tvnpbzb)",
        "Napur Ryvn sh nffhagb crepué, dhnaqb vy fhb pbecb ren dhnttvù, freoò frzcer iretvar yn fhn crefban. Napur ary grzcvb qv Qvb sva qnyyn zvn vasnamvn ub vzcnengb pur yn iretvavgà chò rffrer zbygb tenqvgn n Qvb\n(Pbqvpr qv Nehaqry 404)",
        "Rppb pur Znevn è tvhagn nyy'rgà qv qbqvpv naav ary grzcvb qry Fvtaber. Nqrffb pur snerzb qv yrv nssvapué aba pbagnzvav vy grzcvb qry Fvtaber?\".\n(Cebgbinatryb qv Tvnpbzb)",
        "Enqhan ghggv tyv hbzvav irqbiv qryyn gevoù qv Tvhqn, btahab cbegv vy fhb onfgbar, r yn nssvqrenv n dhryyb fhy dhnyr vy Fvtaber znavsrfgreà ha frtab.\n(Pbqvpr qv Nehaqry 404)",
        "Shebab nyyben niiregvgv ghggv dhryyv qryyn gevoù qv Tvhqn nssvapué, vy tvbeab frthragr, pbybeb pur renab framn zbtyvr, iravffreb cbegnaqb va znab vy cebcevb onfgbar\n(Pbqvpr qv Nehaqry 404)",
        "Cbegngv shbev v onfgbav, n btahab qnin vy fhb: zn va rffv aba i'ren nypha frtab, aba rffraqb hfpvgn yn pbybzon qn nypha onfgbar.\n(Pbqvpr qv Nehaqry 404)",
        "Tvhfrccr fv niivpvaò, cerfr vy fhb onfgbar r nccneir vy frtab: rppb, han pbybzon oryyvffvzn cvù pnaqvqn qryyn arir hfpì qny onfgbar qv Tvhfrccr r fv cbfr fhy fhb pncb.\n(Pbqvpr qv Nehaqry 404)",
        "Aryyn ghn irppuvnvn frv fgngb sryvpr, niraqbgv Qvb erfb vqbarb n evprirer Znevn.\n(Pbqvpr qv Nehaqry 404)",
        "Zn Tvhfrccr fv bccbfr, qvpraqb: \"Ub svtyv r fbab irppuvb, zrager yrv è han entnmmn. Aba ibeerv qviragner bttrggb qv fpureab cre v svtyv qv Vfenryr\".\n(Cebgbinatryb qv Tvnpbzb)",
        "Vy fnpreqbgr qvffr: \"Puvnzngrzv qryyr iretvav framn znppuvn qryyn gevoù qv Qnivq\". V zvavfgev naqnebab, prepnebab, r gebinebab frggr iretvav.\n(Cebgbinatryb qv Tvnpbzb)",
        "Yr vagebqhffreb cbv ary grzcvb qry Fvtaber, r vy fnpreqbgr qvffr: \"Fh, gvengr n fbegr puv svyreà y'beb, y'nzvnagb, vy ovffb, yn frgn, vy tvnpvagb, yb fpneynggb r yn cbecben trahvan\".\n(Cebgbinatryb qv Tvnpbzb)",
        "Vy cbagrsvpr Novngne evfcbfr: \"Cre cnffner vy grzcb, yr fnenaab qngr pvadhr iretvav svab ny tvbeab fgnovyvgb ary dhnyr yn ceraqrenv: aba cbgeà, vasnggv, havefv nq nygev va zngevzbavb\".\n(Inatryb qryyb Cfrhqb-Znggrb)",
        "Nyyben Tvhfrccr cerfr Znevn pba yr pvadhr iretvav pur qbirinab erfgner pba yrv aryyn pnfn qv Tvhfrccr. Dhrfgr iretvav renab: Erorppn, Frsben, Fhfnaan, Novtrn r Pnry.\n(Inatryb qryyb Cfrhqb-Znggrb)",
        "Frv orngn, b Znevn, cbvpué ary ghb hgreb unv cercnengb han novgnmvbar cre vy Fvtaber.\n(Inatryb qryyb Cfrhqb-Znggrb)",
        "Dhnaqb niiraareb dhrfgv zvfgrev, yrv nirin frqvpv naav. Dhnaqb tvhafr cre yrv vy frfgb zrfr, rppb pur Tvhfrccr gbeaò qnyyr fhr pbfgehmvbav r, ragengb va pnfn, yn gebiò vapvagn.\n(Cebgbinatryb qv Tvnpbzb)",
        "Evfcbfr Tvhfrccr: \"Crepué zv yhfvatngr nssvapué vb perqn pur y'natryb qry Fvtaber y'un vatenivqngn? Chò rffrer pur dhnyphab y'noovn vatnaangn svatraqbfv natryb qry Fvtaber\".\n(Inatryb qryyb Cfrhqb-Znggrb)",
        "V cevapvcv qrv fnpreqbgv qvffreb n Tvhfrccr: \"Pur pbf'è pvò pur irqvnzb? Unv cerfb han snapvhyyn iretvar qn phfgbqver va pnfn ghn, r rppb pur è vapvagn\".\n(Pbqvpr qv Nehaqry 404)",
        "Gh unv, vaireb, pnecvgb yr fhr abmmr framn abgvsvpneyb nv svtyv qv Vfenryr, r aba unv ibyhgb cvrtner vy ghb pncb fbggb yn znab qry Qvb baavcbgragr nssvapué orarqvprffr yn ghn qvfpraqramn. Ben, eraqv Znevn iretvar pbzr y'unv evprihgn qny grzcvb qry Fvtaber Qvb.\n(Pbqvpr qv Nehaqry 404)",
        "Dhrfgn è y'npdhn pur qrahapvn v crppngbev: n puv yn nffnttvn qbcb nire zragvgb, Qvb tyv sneà nccnever ha frtab fhyyn snppvn r ha ghzber fhy srzber qrfgeb.\n(Pbqvpr qv Nehaqry 404)",
        "Fr gh aba yr nirffv hfngb ivbyramn, ryyn fneroor evznfgn aryyn fhn iretvavgà.\n(Inatryb qryyb Cfrhqb-Znggrb)",
        "Zrager Tvhfrccr r Znevn pnzzvaninab yhatb yn fgenqn pur pbaqhpr n Orgyrzzr, Znevn qvffr n Tvhfrccr: \"Irqb qninagv n zr qhr cbcbyv, hab cvnatr r y'nygeb è pbagragb\". Tvhfrccr yr evfcbfr: \"Fgnggrar frqhgn fhy ghb tvhzragb r aba qver cnebyr fhcresyhr\".\n(Inatryb qryyb Cfrhqb-Znggrb)",
        "Fvtaber, tena Qvb, noov cvrgà! Cbvpué aba fv è napben znv hqvgb aé ivfgb aé fbfcrggngb pur yr znzzryyr fvnab cvrar qv ynggr r vy angb znfpuvrggb qvzbfgev pur fhn znqer è iretvar. Ary arbangb aba iv è nyphan pbagnzvanmvbar qv fnathr, arffha qbyber nccner aryyn cnegbevragr. Un pbaprcvgb iretvar, iretvar un cnegbevgb, r qbcb nirer cnegbevgb evznar iretvar.\n(Pbqvpr qv Nehaqry 404)",
        "Fnybzr zvfr vy fhb qvgb aryyn anghen qv yrv, r znaqò ha tevqb, qvpraqb: \"Thnv nyyn zvn vavdhvgà r nyyn zvn vaperqhyvgà, crepué ub gragngb vy Qvb ivib rq rppb pur ben yn zvn znab fv fgnppn qn zr, oehpvngn\".\n(Cebgbinatryb qv Tvnpbzb)",
        "Zrager tenaqrzragr zv fghcvib pur aba cvnatrffr pbzr fbab fbyvgv cvnatrer v onzovav nccran angv, r yb grarib, thneqnaqbyb va ibygb, rtyv zv fbeevfr pba ha fbeevfb tvbpbaqvffvzb. Nceì tyv bppuv, zv svffò nphgnzragr r fhovgb, qnv fhbv bppuv, hfpì han tenaqr yhpr pbzr ha tenaqr ynzcb.\n(Pbqvpr qv Nehaqry 404)",
        "Rtyv qvffr pur Trfù cneyò dhnaqb ren napben aryyn phyyn r qvffr n fhn znqer Znevn: \"Vb fbab Trfù svtyvb qv Qvb, vy Ybtb, qn gr trarengb frpbaqb dhnagb gv nirin naahamvngb y'natryb Tnoevryr. Zvb cnqer zv un vaivngb cre yn fnyirmmn qry zbaqb\"\n(Vy Inatryb Nenob qryy'Vasnamvn)",
        "Tyv fgrffv navznyv, vy ohr r y'nfvab, yb nirinab va zrmmb n ybeb r yb nqbeninab qv pbagvahb. Fv nqrzcì nyyben dhnagb ren fgngb qrggb qny cebsrgn Nonphp, pba yr cnebyr: \"Gv snenv pbabfprer va zrmmb n qhr navznyv\".\n(Inatryb qryyb Cfrhqb-Znggrb)",
        "Fvtaber, gv cbffb gbppner b cevzn gv qrib nqbener?\n(Pbqvpr qv Nehaqry 404)",
        "Pvepbapvfreb qhadhr aryyn tebggn; dhryyn irppuvn roern cerfr dhrfgn zrzoenan, frpbaqb nygev vairpr rffn cerfr vy pbeqbar bzoryvpnyr, r yn zvfr va han nzcbyyn qv irppuvb byvb qv aneqb. Dhrfgn è y'nzcbyyn pur sh va frthvgb pbzcengn qn Znevn, yn crppngevpr, dhryyn pur irefò fhy pncb r fhv cvrqv qry fvtaber abfgeb Trfù Pevfgb r nfpvhtò cbv pba v fhbv pncryyv.\n(Vy Inatryb Nenob qryy'Vasnamvn)",
        "Btav znfpuvb pur ncer yn ihyin fneà puvnzngb fnagb qv Qvb.\n(Vy Inatryb Nenob qryy'Vasnamvn)",
        "Rppb, nyy'vaterffb unaab fnyhgngb vy entnmmb r fv fbab cebfgengv n green; y'nqbenab frpbaqb vy pbfghzr qrv oneonev r hab nyyn ibygn onpvnab v cvrqv qry onzovab. Pur pbfn fgnaab snpraqb? Aba irqb orar.\n(Pbqvpr qv Nehaqry 404)",
        "Tyv qvffr Tvhfrccr: \"Dhrfgv hbzvav unaab snggb zbygb orar n aba onpvner tengvf vy onzovab, r aba pbzr dhrv abfgev cnfgbev pur iraareb dhv framn qbav\"\n(Pbqvpr qv Nehaqry 404)",
        "Tvhagv n han tebggn ibyyreb evcbfnefv. Yn orngn Znevn qvfprfr qny tvhzragb r, frqhgn, grarin vy onzovab Trfù fhy fhb terzob. Pba Tvhfrccr p'renab ger entnmmv r pba Znevn han entnmmn pur snprinab yn fgrffn fgenqn. Vzcebiivfnzragr qnyyn tebggn hfpvebab zbygv qentuv.\nNyyben Trfù fprfr qny terzob qv fhn znqer, fgrggr qevggb fhv fhbv cvrqv qninagv nv qentuv: rffv creò nqbenebab Trfù r cbv fr ar naqnebab ivn.\nZnevn r Tvhfrccr grzrinab pur vy onzovab sbffr zbefb qnv qentuv; zn Trfù qvffr: \"Aba grzrgr, r aba crafngr pur vb fvn ha onzovab. Vb vasnggv fbab frzcer fgngb cresrggb r yb fbab ghggben: è arprffnevb pur qninagv n zr ghggr yr orfgvr fryingvpur qviragvab znafhrgr\".\n(Inatryb qryyb Cfrhqb-Znggrb)",
        "Gnyr fnpreqbgr nirin ha svtyvb qv ger naav, cbffrqhgb qn nyphav qrzbav, pur cneynin qv zbygr pbfr; r dhnaqb v qrzbav fv vzcnqebavinab qv yhv fv fgenccnin yr irfgv, erfgnin ahqb, r gvenin fnffv ntyv hbzvav.\n(Vy Inatryb Nenob qryy'Vasnamvn)",
        "Fv qverffreb nyyn pnfn qv ha hbzb fcbfngb qn cbpb grzcb zn, pbycvgb qn znyrsvmvb, aba cbgrin tbqrefv yn zbtyvr. Cnffngn yn abggr cerffb qv yhv, prffò y'vasyhffb qry znyrsvmvb.\n(Vy Inatryb Nenob qryy'Vasnamvn)",
        "Yn snapvhyyn qvffr: \"Pbz'è yn snppraqn qv dhrfgb zhyb, zvr fvtaber?\". Cvnatraqb, rffr evfcbfreb: \"Vy zhyb pur gh irqv ren abfgeb sengryyb, angb qnyyn fgrffn abfgen znqer\"\n(Vy Inatryb Nenob qryy'Vasnamvn)",
        "Fvtaber Trfù va Rtvggb srpr zbygv zvenpbyv pur aba fv gebinab fpevggv aé ary Inatryb qryy'vasnamvn, aé ary Inatryb pbzcyrgb.\n(Vy Inatryb Nenob qryy'Vasnamvn)",
        "Yn qbaan yrooebfn naqngn n gebiner yn fvtaben cnqeban Znevn znqer qv Trfù, yr qvffr: \"Fvtaben zvn nvhgnzv!\". Yn cnqeban Znevn evfcbfr: \"Pur nvhgb ihbv? Ihbv beb r netragb? B pur vy ghb pbecb fvn zbaqngb qnyyn yrooen?\"\n(Vy Inatryb Nenob qryy'Vasnamvn)",
        "Thnv, thnv n zr! Aba p'è cebcevb arffhab pur zv yvorev qn dhrfgb crffvzb qentbar.\n(Vy Inatryb Nenob qryy'Vasnamvn)",
        "Qnv cnaabyvav qry fvtaber Trfù cerfr cbv han snfpvn pur qvrqr nyyn snapvhyyn qvpraqb: \"Ceraqv dhrfgn snfpvn r zbfgenyn ny ghb arzvpb btav ibygn pur yb irqenv\". R, pba v fnyhgv, yr pbatrqò.\n(Vy Inatryb Nenob qryy'Vasnamvn)",
        "Vy fvtaber Trfù qvffr nyyben nv entnmmv: \"Nyyr svthevar pu'vb ub snggb beqvareò qv pnzzvaner\". Nyyben rffv tyv qbznaqninab: \"Frv gh vy svtyvb qry Perngber?\". R vy fvtaber Trfù beqvaò n rffr qv pnzzvaner: fhovgb fv zvfreb n fnygner r cbv, cre fhn pbaprffvbar, fv neerfgnebab ahbinzragr.\nNyybagnangvfv cbv v entnmmv enppbagnebab dhrfgr pbfr nv travgbev; v ybeb cnqev qvffreb ybeb: \"Thneqngriv, svtyv, qny ceraqrer snzvyvnevgà pba yhv, è ha zntb crevpbybfb. Shttvgryb qhadhr rq rivgngryb, r qv dhv va ninagv aba tvbpngr cvù pba yhv\".\n(Vy Inatryb Nenob qryy'Vasnamvn)",
        "Ragengb aryyn bssvpvan qry gvagber, vy fvtaber Trfù cerfr ghggv dhrv cnaav r yv trggò va han obggr cvran qv nmmheeb vaqvnab.\n(Vy Inatryb Nenob qryy'Vasnamvn)",
        "Btav ibygn pur Tvhfrccr nirin ovfbtab qv yhv ary fhb ynibeb, cre nyyhatner, nppbepvner, nyynetner b erfgevatrer fvn qv ha phovgb pur qv ha frzvphovgb dhnypur pbfn, vy fvtaber Trfù fgraqrin yn fhn znab irefb qv rffn r fhovgb qviragnin pbzr qrfvqrenin Tvhfrccr; aé dhrfgv nirin ovfbtab qv sner dhnypbfn pba yr znav. Tvhfrccr aba ren vasnggv qv han novyvgà fgenbeqvanevn aryy'negr qv pnecragvrer.\n\"Tvhfrccr, ibtyvb pur gh zv snppvn ha gebab qryyn fgrffn zvfhen qv dhryyb fhy dhnyr fbab fbyvgb frqrer\". Tvhfrccr nppbafragì r fhovgb qvrqr znab nyy'bcren: erfgò aryyn erttvn cre qhr naav cbegnaqb pbfì n grezvar yn snooevpnmvbar qry gebab. Snggbyb genfcbegner ny fhb cbfgb, fv nppbefr pur qn btav yngb znapninab qhr frzvphovgv cre enttvhatrer yn zvfhen rfnggn. Vagreebtngb qny fvtaber Trfù fhyyn pnhfn qry fhb gvzber, Tvhfrccr evfcbfr: \"Crepué ub creqhgb ghggb dhnagb ub snggb va dhrv qhr naav\".\nVy fvtaber Trfù tyv evfcbfr: \"Aba grzrer, aba gv noonggrer. Gh nssreen ha yngb qry gebab, vb nssreereò y'nygeb r pbfì yb cbegrerzb n cnev\".\nTvhfrccr srpr pbzr nirin qrggb vy fvtaber Trfù; btahab gveò vy cebcevb yngb r vy gebab sh evcnengb r pbaqbggb nyyn tvhfgn zvfhen.\n(Vy Inatryb Nenob qryy'Vasnamvn)",
        "Nyyben, puvnzngb qny fvtaber, vy frecragr fv srpr ninagv ghggb fbggbzrffb. Rtyv qvffr: \"In' n fhppuvner ghggb vy iryrab pur unv vavrggngb va dhrfgb entnmmb\". Vy frecragr fv niivpvaò ny entnmmb r fhppuvò ghggb vy fhb iryrab.  Cbv vy fvtaber Trfù yb znyrqvffr r fhovgb fpbccvò.\n(Vy Inatryb Nenob qryy'Vasnamvn)",
        "Ha tvbeab, zrager vy fvtaber Trfù fgnin ahbinzragr pba qrv entnmmv pur tvbpninab fh qv ha grggb, ha entnmmb pnqqr qnyy'nygb qry greenmmb, r fhovgb fcveò.\nDhnaqb tvhafreb v cneragv qv dhry entnmmb zbegb, qvffreb ny fvtaber Trfù: \"Frv gh pur unv snggb cerpvcvgner abfgeb svtyvb qny grggb\".\nNyyben vy fvtaber Trfù qvfprfr r fgnaqb fhy zbegb tevqò n tena ibpr: \"Mrabar, Mrabar, puv gv un snggb pnqrer qny grggb?\". Vy zbegb evfcbfr: \"Fvtaber aba frv gh pur zv unv snggb pnqrer, zn b qrvan zv un ohggngb tvù\".\n(Vy Inatryb Nenob qryy'Vasnamvn)",
        "Vy svtyvb qryyb fpevon Naan fr ar fgnin yà pba Tvhfrccr r, cerfb ha enzb qv fnyvpr, snprin fpbeerer ivn yr npdhr enppbygr qn Trfù.\nDhnaqb Trfù ivqr pvò pur nppnqrin, fqrtangb tyv qvffr: \"B pnggvib, rzcvb, vafrafngb! Pur znyr gv unaab snggb yr sbffr r yr npdhr? Gh cher, rppb pur gv frppurenv pbzr ha nyoreb; aba zrggrenv aé sbtyvr, aé enqvpv, aé sehggb\".\nFhovgb dhry entnmmb fv frppò ghggb.\n(Inatryb qryy'Vasnamvn qv Gbzznfb)",
        "Hegngb qn ha entnmmb. Qbcb qv pvò pnzzvanin cre vy ivyynttvb, dhnaqb ha entnmmb, pbeeraqb, naqò n hegner pbageb yn fhn fcnyyn. Trfù, veevgngb, tyv qvffr: \"Aba crepbeerenv ghggn yn ghn fgenqn!\". R fhovgb pnqqr zbegb.\n(Inatryb qryy'Vasnamvn qv Gbzznfb)",
        "Nymnebab nyyben yn ibpr v travgbev qry zbegb pbageb Znevn r Tvhfrccr; qvprinab ybeb: \"Ibfgeb svtyvb un znyrqrggb vy abfgeb svtyvb rq è zbegb\".\n(Inatryb qryyb Cfrhqb-Znggrb)",
        "Tvhagn qn yhv yn znqer yb certò qvpraqb: \"Fvtaber zvb, pur un snggb znv pbfghv cre zbever?\". Rtyv yr evfcbfr: \"Ren qrtab qv zbegr, niraqb znaqngb nyy'nevn dhnagb vb nirib snggb\".\n(Inatryb qryyb Cfrhqb-Znggrb)",
        "Tvhfrccr, puvnzngb vy entnmmb va qvfcnegr, yb nzzbavin qvpraqb: \"Crepué snv gnyv pbfr? Pbfgbeb ar fbssebab, pv bqvnab r crefrthvgnab\". Trfù evfcbfr: \"Vb fb pur dhrfgr ghr cnebyr aba fbab ghr. Ghggnivn fgneò mvggb cre gr; zn dhryyv cbegrenaab yn ybeb chavmvbar\". R fhovgb tyv npphfngbev qviraareb pvrpuv.\n(Inatryb qryy'Vasnamvn qv Gbzznfb)",
        "Irqraqb pur Trfù nirin snggb han gnyr pbfn, Tvhfrccr fv nymò, tyv cerfr y'berppuvb r tyvryb gveò sbegr. Vy entnmmb nyyben fv fqrtaò r tyv qvffr: \"N gr onfgv prepner r aba gebiner! Irenzragr aba unv ntvgb va zbqb frafngb. Aba fnv pur fbab ghb? Aba zv zbyrfgner!\".\n(Inatryb qryy'Vasnamvn qv Gbzznfb)",
        "R vy entnmmb fr ar gbeaò n pnfn qn Tvhfrccr. Zn Tvhfrccr ar sh enggevfgngb r beqvaò n fhn znqer: \"Aba ynfpvneyb hfpver shbev qryyn cbegn, crepué ghggv dhryyv pur yb veevgnab, zhbvbab\".\n(Inatryb qryy'Vasnamvn qv Gbzznfb)",
        "Zn va dhry zbzragb Trfù cerfr cre y'berppuvb vy snapvhyyb zbegb, yb graar fbfcrfb qn green nyyn cerframn qv ghggv, r ivqreb Trfù cneyner pba yhv pbzr sn ha cnqer pba fhb svtyvb. Vy fhb fcvevgb evgbeaò va yhv rq rtyv evivffr. R ghggv ar shebab fghcvgv.\n(Inatryb qryyb Cfrhqb-Znggrb)",
        "Niraqb vy znrfgeb zvanppvngb qv onfgbaneyb, vy fvtaber Trfù tyv rfcbfr v fvtavsvpngv qryyr yrggrer nyrs r org.\nCebahapvngb pur roor y'nyrs, vy znrfgeb tyv beqvaò qv cebahapvner org. Zn vy fvtaber Trfù tyv evfcbfr: \"Qvzzv cevzn vy fvtavsvpngb qv nyrs, r cbv vb cebahapreò org\". Niraqb vy znrfgeb nymngb yn znab cre sehfgneyb, fhovgb dhryyn znab vanevqì, rq rtyv zbeì.\nNyyben Tvhfrccr qvffr nyyn cnqeban Znevn: \"Qv dhv va cbv aba ynfpvnzbyb cvù hfpver qv pnfn. Puvhadhr vasnggv yb pbagenevn è pbycvgb n zbegr\n(Vy Inatryb Nenob qryy'Vasnamvn)",
        "Tyv qbznaqò cbv vy pncb qrv qbggbev: \"Unv yrggb v yvoev?\". \"Ub yrggb fvn v yvoev, – evfcbfr vy fvtaber Trfù – fvn dhnagb è va rffv pbagrahgb\".\n(Vy Inatryb Nenob qryy'Vasnamvn)",
        "P'ren yà ha svybfbsb crevgb va nfgebabzvn vy dhnyr qbznaqò n Trfù fr nirffr fghqvngb nfgebybtvn. Vy fvtaber Trfù evfcbfr rfcbaraqb vy ahzreb qryyr fsrer r qrv pbecv pryrfgv, yn ybeb anghen r yr ybeb bcrenmvbav, yn ybeb pbagenccbfvmvbar, vy ybeb nfcrggb gevnatbyner, dhnqengb rq rfntbanyr, yn ybeb genvrggbevn r yn ybeb cbfvmvbar qv zvahgb va frpbaqb, r zbygr nyger pbfr veenttvhatvovyv nyyn entvbar.\n(Vy Inatryb Nenob qryy'Vasnamvn)",
        "Evcvtyvngryb, qhadhr, gr ar certb, sengryyb Tvhfrccr. Aba cbffb fbccbegner y'nhfgrevgà qry fhb fthneqb, aba fb cebcevb fcvrtnezv vy fhb cneyner. Dhrfgb entnmmb aba è angb greerfger: chò qbzner crefvab vy shbpb!\n(Inatryb qryy'Vasnamvn qv Gbzznfb)",
        "Trfù tyv qvffr: \"Fr irenzragr frv ha znrfgeb r fnv orar yr yrggrer, qvzzv vy inyber qryy'Nysn r vb gv qveò dhryyb qryyn Orgn\". Zn vy znrfgeb fv fqrtaò r yb cvppuvò fhyyn grfgn: vy entnmmb fv fragì znyr r yb znyrqvffr. Fhovgb dhryyb firaar r pnqqr obppbav n green.\n(Inatryb qryy'Vasnamvn qv Gbzznfb)",
        "Cevzn, qhadhr, gh qì pur pbf'è yn grg, r vb cbv gv qveò pur pbf'è y'nyrs.\n(Inatryb qryyb Cfrhqb-Znggrb)",
        "Gen dhrv svybfbsv ir a'ren napur hab qbggvffvzb aryyr fpvramr anghenyv. Dhrfgv vagreebtò Trfù fr nirffr fghqvngb zrqvpvan; rtyv evfcbfr rfcbaraqb yn svfvpn, yn zrgnsvfvpn, y'vcresvfvpn r y'vcbsvfvpn, yr sbemr qry pbecb, tyv hzbev r v ybeb rssrggv.\n(Vy Inatryb Nenob qryy'Vasnamvn)",
        "Vapbzvapvò nyyben n tevqner n dhnagv y'hqvinab, qvpraqb: \"Pbzr chò ivirer fhyyn green pbfghv? Ny pbagenevb, è qrtab qv rffrer nccrfb n han tenaqr pebpr\".\n(Inatryb qryyb Cfrhqb-Znggrb)",
        "Nyyben Trfù cerfr n qver ny cbcbyb: \"Dhnagb yr orfgvr fbab zvtyvbev qv ibv!\"\n(Inatryb qryyb Cfrhqb-Znggrb)"
    )

    override fun execute() {
        val giorniTrascorsi = Period.between(primoDiNovembre, LocalDate.now()).days
        val index = giorniTrascorsi.mod(vangeloDellInfanziaSecondoSarrusofono.length().toInt())
        val rot13String = rot13(vangeloDellInfanziaSecondoSarrusofono[index])

        botUtils.messaggio(ActionResponse.message(rot13String))
    }

    override fun firstRun(): LocalDateTime {
        val firstRun = primoDiNovembre.atTime(dieciDiMattina)
        if (LocalDateTime.now() > firstRun) {
            // già iniziato!
            return nextRun()
        }
        return firstRun
    }

    override fun nextRun(): LocalDateTime {
        val nextRun = LocalDate.now().atTime(dieciDiMattina)
        if (LocalTime.now() > dieciDiMattina) {
            // domani
            return nextRun.plusDays(1)
        }
        return nextRun
    }

    private fun rot13(inputString: String): String {
        return inputString.map {
            when (it) {
                in 'a'..'m', in 'A'..'M' -> it + 13
                in 'n'..'z', in 'N'..'Z' -> it - 13
                else -> it
            }
        }.joinToString("")
    }

}