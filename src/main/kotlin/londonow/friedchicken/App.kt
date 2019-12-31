/*
 * This Kotlin source file was generated by the Gradle 'init' task.
 */
package londonow.friedchicken

import be.zvz.kotlininside.KotlinInside
import be.zvz.kotlininside.api.article.ArticleWrite
import be.zvz.kotlininside.api.type.Article
import be.zvz.kotlininside.api.type.HeadText
import be.zvz.kotlininside.api.type.content.StringContent
import be.zvz.kotlininside.http.DefaultHttpClient
import be.zvz.kotlininside.session.user.LoginUser
import org.slf4j.LoggerFactory
import java.util.*
import java.util.concurrent.Executors


internal object App {
    private val logger = LoggerFactory.getLogger(App::class.java)
    private val threadPool = Executors.newCachedThreadPool()

    @JvmStatic
    fun main(args: Array<String>) {
        val scanner = Scanner(System.`in`)

        print("DCInside 아이디를 입력해주세요 > ")
        val id = scanner.nextLine()
        print("비밀번호를 입력해주세요 > ")
        val password = scanner.nextLine()

        logger.info("로그인 중...")
        try {
            KotlinInside.createInstance(
                    LoginUser(id, password),
                    DefaultHttpClient(true, true)
            )
        } catch (e: NullPointerException) {
            logger.error("로그인하지 못했습니다. 아이디와 비밀번호를 확인해주세요.")
        }
        logger.info("로그인 완료!")

        logger.info("타이머 생성 중")

        val timer = Timer()

        val calendar = Calendar.getInstance();
        calendar.set(
                2019,
                12,
                31,
                23,
                59,
                0
        )

        timer.schedule(object: TimerTask() {
            override fun run() {
                threadPool.submit {
                    writeArticle("owgenji")
                }
            }
        }, 60 * 1000)

        logger.info("타이머 생성 완료!")
        logger.info("글 작성 대기 중...")
    }

    private fun writeArticle(gallId: String?, headText: HeadText? = null) {
        if (gallId == null) {
            logger.error("글 작성 실패! - gallId 값이 null입니다.")
            return
        }

        val articleWrite = ArticleWrite(
                gallId = gallId,
                article = Article(
                        subject = "새해 첫글",
                        content = listOf(
                                StringContent("ㅇㅇ")
                        ),
                        headText = headText
                ),
                session = KotlinInside.getInstance().session
        )

        lateinit var result: ArticleWrite.WriteResult

        loop@ do {
            result = articleWrite.write()
            when {
                !result.result -> {
                    logger.error("글 작성 실패, 다시 시도합니다.")
                    logger.error(result.toString())
                    Thread.sleep(500)
                    result = articleWrite.write()
                }
                else -> {
                    logger.info("글 작성 완료 : ")
                    logger.info(result.toString())
                    break@loop
                }
            }
        } while (!result.result)
    }
}
