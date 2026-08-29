package com.example.data.ai

import android.util.Log
import com.example.BuildConfig
import com.example.data.model.AiChatMessage
import com.example.data.model.SectionType
import com.example.data.model.WebsiteSection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.UUID
import java.util.concurrent.TimeUnit

class GeminiAiService {
    private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    private val jsonMediaType = "application/json; charset=utf-8".toMediaType()

    suspend fun generateWithGemini(prompt: String, systemInstruction: String? = null): String = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext fallbackResponse(prompt)
        }

        try {
            val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=$apiKey"
            val contentsArray = JSONArray().apply {
                put(JSONObject().apply {
                    put("parts", JSONArray().apply {
                        put(JSONObject().apply {
                            put("text", prompt)
                        })
                    })
                })
            }

            val requestBodyJson = JSONObject().apply {
                put("contents", contentsArray)
                if (!systemInstruction.isNullOrBlank()) {
                    put("systemInstruction", JSONObject().apply {
                        put("parts", JSONArray().apply {
                            put(JSONObject().apply {
                                put("text", systemInstruction)
                            })
                        })
                    })
                }
            }

            val request = Request.Builder()
                .url(url)
                .post(requestBodyJson.toString().toRequestBody(jsonMediaType))
                .build()

            val response = client.newCall(request).execute()
            val responseBody = response.body?.string()

            if (response.isSuccessful && !responseBody.isNullOrEmpty()) {
                val json = JSONObject(responseBody)
                val candidates = json.optJSONArray("candidates")
                if (candidates != null && candidates.length() > 0) {
                    val candidate = candidates.getJSONObject(0)
                    val content = candidate.optJSONObject("content")
                    val parts = content?.optJSONArray("parts")
                    if (parts != null && parts.length() > 0) {
                        val text = parts.getJSONObject(0).optString("text", "")
                        if (text.isNotBlank()) return@withContext text
                    }
                }
            }
            return@withContext fallbackResponse(prompt)
        } catch (e: Exception) {
            Log.e("GeminiAiService", "API error, falling back", e)
            return@withContext fallbackResponse(prompt)
        }
    }

    suspend fun processVerseChat(userInput: String): AiChatMessage = withContext(Dispatchers.Default) {
        val lower = userInput.lowercase()
        var suggestedToolId: String? = null
        var suggestedToolName: String? = null

        when {
            lower.contains("website") || lower.contains("landing page") || lower.contains("web page") || lower.contains("restaurant site") -> {
                suggestedToolId = "website_builder"
                suggestedToolName = "AI Website Builder"
            }
            lower.contains("caption") || lower.contains("post") || lower.contains("hashtag") || lower.contains("instagram") || lower.contains("tweet") -> {
                suggestedToolId = "caption_gen"
                suggestedToolName = "Caption Generator"
            }
            lower.contains("thumbnail") || lower.contains("youtube") -> {
                suggestedToolId = "thumbnail_maker"
                suggestedToolName = "Thumbnail Maker"
            }
            lower.contains("code") || lower.contains("javascript") || lower.contains("html") || lower.contains("css") || lower.contains("function") || lower.contains("bug") -> {
                suggestedToolId = "code_editor"
                suggestedToolName = "Online Code Editor"
            }
            lower.contains("video script") || lower.contains("script") || lower.contains("reel") || lower.contains("shorts") -> {
                suggestedToolId = "script_gen"
                suggestedToolName = "AI Video Script Generator"
            }
            lower.contains("background") || lower.contains("remove bg") -> {
                suggestedToolId = "bg_remover"
                suggestedToolName = "Background Remover"
            }
            lower.contains("resize") || lower.contains("compress") || lower.contains("photo") || lower.contains("image") -> {
                suggestedToolId = "image_resizer"
                suggestedToolName = "Image Resizer"
            }
            lower.contains("movie") || lower.contains("film") || lower.contains("watch") -> {
                suggestedToolId = "movie_discovery"
                suggestedToolName = "Movie Discovery"
            }
            lower.contains("regex") -> {
                suggestedToolId = "regex_tester"
                suggestedToolName = "Regex Tester"
            }
            lower.contains("json") -> {
                suggestedToolId = "json_formatter"
                suggestedToolName = "JSON Formatter"
            }
        }

        val systemPrompt = "You are Verse AI, the ultra-smart creative AI assistant inside ToolVerse AI platform. Provide concise, helpful, SaaS-caliber advice and guide the user to the best tools."
        val responseText = generateWithGemini(
            prompt = "User asked: '$userInput'. Provide a helpful, clear, enthusiastic 2-4 sentence response guiding them on how to accomplish this in ToolVerse AI.",
            systemInstruction = systemPrompt
        )

        AiChatMessage(
            id = UUID.randomUUID().toString(),
            sender = "verse_ai",
            message = responseText,
            suggestedToolId = suggestedToolId,
            suggestedToolName = suggestedToolName
        )
    }

    fun generateWebsiteSectionsFromPrompt(prompt: String): List<WebsiteSection> {
        val lower = prompt.lowercase()
        val isRestaurant = lower.contains("restaurant") || lower.contains("cafe") || lower.contains("food") || lower.contains("bakery") || lower.contains("dining")
        val isTech = lower.contains("tech") || lower.contains("software") || lower.contains("saas") || lower.contains("app") || lower.contains("ai")
        val isPortfolio = lower.contains("portfolio") || lower.contains("resume") || lower.contains("photography") || lower.contains("design")

        return when {
            isRestaurant -> listOf(
                WebsiteSection(
                    id = "hero",
                    type = SectionType.HERO,
                    title = "Artisan Culinary Experience",
                    subtitle = "Farm-to-table gourmet gastronomy crafted with local organic ingredients.",
                    content = "Reserve your table today for an unforgettable dining experience in downtown.",
                    buttonText = "Book a Table",
                    imageUrl = "https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?w=800"
                ),
                WebsiteSection(
                    id = "menu",
                    type = SectionType.MENU_GALLERY,
                    title = "Signature Chef Menu",
                    subtitle = "Seasonal dishes and award-winning pairings",
                    content = "Handcrafted pastas, dry-aged steaks, woodfired artisan pizzas, and decadent desserts.",
                    items = listOf(
                        "Truffle Tagliolini - Handmade pasta with shaved black winter truffles",
                        "Prime Woodfired Ribeye - Herb butter, charred asparagus, red wine reduction",
                        "Wild King Salmon - Crispy skin, celery root purée, lemon caper emulsion",
                        "Decadent Dark Chocolate Soufflé - Madagascar vanilla bean gelato"
                    ),
                    imageUrl = "https://images.unsplash.com/photo-1555396273-367ea4eb4db5?w=800"
                ),
                WebsiteSection(
                    id = "about",
                    type = SectionType.ABOUT,
                    title = "Our Culinary Story",
                    subtitle = "Passion for authentic flavors since 2012",
                    content = "Founded by Master Chef Marco Rossi, our kitchen fuses traditional European heritage with vibrant modern culinary techniques.",
                    imageUrl = "https://images.unsplash.com/photo-1578474846511-04ba529f0b88?w=800"
                ),
                WebsiteSection(
                    id = "contact",
                    type = SectionType.CONTACT_BOOKING,
                    title = "Online Reservations & Inquiries",
                    subtitle = "Open Tuesday - Sunday for Lunch & Dinner",
                    content = "124 Culinary Boulevard, Metropolitan District | +1 (555) 382-9011 | reservations@artisandining.com",
                    buttonText = "Confirm Reservation"
                ),
                WebsiteSection(
                    id = "footer",
                    type = SectionType.FOOTER,
                    title = "Artisan Dining Co.",
                    subtitle = "© 2026 Artisan Dining. All rights reserved. Created with ToolVerse AI.",
                    content = "Instagram: @artisan_dining | Facebook | Twitter"
                )
            )

            isTech -> listOf(
                WebsiteSection(
                    id = "hero",
                    type = SectionType.HERO,
                    title = "Next-Gen Cloud Intelligence Platform",
                    subtitle = "Automate workflows, optimize telemetry, and deploy AI models at scale.",
                    content = "Supercharge your development cycle with automated CI/CD and multi-cloud sync.",
                    buttonText = "Start Free Trial",
                    imageUrl = "https://images.unsplash.com/photo-1451187580459-43490279c0fa?w=800"
                ),
                WebsiteSection(
                    id = "features",
                    type = SectionType.FEATURES,
                    title = "Engineered for High-Velocity Teams",
                    subtitle = "Everything required to scale mission-critical systems",
                    content = "Zero latency observability, automated rollbacks, and enterprise-grade security compliance.",
                    items = listOf(
                        "Real-time Distributed Tracing with sub-millisecond overhead",
                        "Automated Canary Deployments and intelligent health rollbacks",
                        "SOC2 Type II & HIPAA certified encryption at rest and in transit"
                    )
                ),
                WebsiteSection(
                    id = "testimonials",
                    type = SectionType.TESTIMONIALS,
                    title = "Trusted by 25,000+ Engineers",
                    subtitle = "What global engineering leaders say about us",
                    content = "“ToolVerse AI generated our core landing flow and reduced our build cycle by 70%.” — Sarah Lin, VP of Engineering"
                ),
                WebsiteSection(
                    id = "contact",
                    type = SectionType.CONTACT_BOOKING,
                    title = "Ready to scale your architecture?",
                    subtitle = "Talk to our enterprise solution architects today.",
                    content = "support@cloudintel.io | 100 Innovation Way, Silicon Valley, CA",
                    buttonText = "Request Demo"
                ),
                WebsiteSection(
                    id = "footer",
                    type = SectionType.FOOTER,
                    title = "CloudIntel SaaS",
                    subtitle = "© 2026 CloudIntel Inc. Built with ToolVerse AI.",
                    content = "Privacy Policy | Terms of Service | Security Status"
                )
            )

            else -> listOf(
                WebsiteSection(
                    id = "hero",
                    type = SectionType.HERO,
                    title = "Crafting Exceptional Digital Experiences",
                    subtitle = "Modern, high-performance creative agency and studio.",
                    content = "We build award-winning digital products, branding, and interactive web ecosystems.",
                    buttonText = "Explore Our Work",
                    imageUrl = "https://images.unsplash.com/photo-1498050108023-c5249f4df085?w=800"
                ),
                WebsiteSection(
                    id = "features",
                    type = SectionType.FEATURES,
                    title = "Our Core Capabilities",
                    subtitle = "Full-spectrum digital craftsmanship",
                    content = "From brand discovery and UI/UX design to modern full-stack development and optimization.",
                    items = listOf(
                        "Brand Identity, Typography & Visual Strategy",
                        "UI/UX Design Systems & High-Fidelity Prototyping",
                        "Cloud-Native Web & Mobile Engineering"
                    )
                ),
                WebsiteSection(
                    id = "about",
                    type = SectionType.ABOUT,
                    title = "Design Driven. Performance Focused.",
                    subtitle = "Over a decade of industry leadership",
                    content = "We partner with visionary founders and global enterprises to build memorable digital products."
                ),
                WebsiteSection(
                    id = "contact",
                    type = SectionType.CONTACT_BOOKING,
                    title = "Let's Build Something Brilliant Together",
                    subtitle = "Have a project in mind? Reach out today.",
                    content = "hello@creativestudio.io | Studio HQ, Soho, New York",
                    buttonText = "Send Message"
                ),
                WebsiteSection(
                    id = "footer",
                    type = SectionType.FOOTER,
                    title = "Creative Studio",
                    subtitle = "© 2026 Creative Studio. Built on ToolVerse AI.",
                    content = "Twitter | Dribbble | GitHub | LinkedIn"
                )
            )
        }
    }

    private fun fallbackResponse(prompt: String): String {
        val lower = prompt.lowercase()
        return when {
            lower.contains("website") -> "I have drafted a high-converting website architecture with responsive Hero, Features, Gallery, and Contact sections! Tap 'AI Website Builder' to preview and customize your layout in real-time."
            lower.contains("caption") -> "Here is your high-engagement caption: '⚡ Elevate your workflow with next-gen precision. Why settle for ordinary when you can craft the extraordinary? 🚀 #Innovation #Productivity #CreativeFlow'"
            lower.contains("thumbnail") -> "Thumbnail Idea: Split-screen high-contrast visual. Left side: Frustrated face with red glitch icon. Right side: Glowing cyan 10X badge with clean UI preview. Bold 3-word title: 'DO THIS INSTEAD!'."
            lower.contains("code") -> "This code utilizes reactive state management and structured asynchronous handling. To optimize performance, remember to memoize costly transformations and prevent redundant recompositions."
            lower.contains("script") -> "[Scene 1 - 0:00-0:03]: Fast zoom into phone screen. 'Are you tired of switching between 10 different apps?' \n[Scene 2 - 0:03-0:08]: High-tempo beat drops showing ToolVerse AI interface. 'Meet ToolVerse: Every digital tool you will ever need, all in one place.' \n[Scene 3 - 0:08-0:15]: Call to action. 'Click the link to start creating for free!'"
            lower.contains("background") -> "Opening the Background Remover workspace! You can upload any JPEG or PNG image to instantly isolate subjects with transparent Alpha channel export."
            lower.contains("resize") -> "Launching Image Resizer: Choose from standard presets (Instagram Post 1080x1080, YouTube Cover 1280x720, Twitter Header 1500x500) or enter custom dimensions."
            else -> "ToolVerse AI is ready to accelerate your workflow. Choose any tool from our 6 categories or ask me for captions, code optimization, scripts, and website generation!"
        }
    }
}
