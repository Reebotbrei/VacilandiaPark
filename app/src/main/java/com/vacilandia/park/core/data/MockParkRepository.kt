package com.vacilandia.park.core.data

import com.vacilandia.park.R
import com.vacilandia.park.feature.attractions.model.Attraction
import com.vacilandia.park.feature.attractions.model.AttractionCategory
import com.vacilandia.park.feature.attractions.model.AttractionGalleryPhoto
import com.vacilandia.park.feature.info.model.ChecklistItem
import com.vacilandia.park.feature.info.model.FaqItem
import com.vacilandia.park.feature.info.model.ParkZone
import com.vacilandia.park.feature.tickets.model.PromoOffer
import com.vacilandia.park.feature.tickets.model.TicketAvailability
import com.vacilandia.park.feature.tickets.model.TicketGroup
import com.vacilandia.park.feature.tickets.model.TicketItem
import com.vacilandia.park.feature.tickets.model.VisitDateOption
import java.util.Calendar

object MockParkRepository {

    const val PARK_NAME = "Vacilandia Park"
    const val PARK_ADDRESS = "Av. Las Gaviotas con Av. El Rosal, Urb. Las Gardenias, Ate, Lima"
    const val PARK_REFERENCE = "A 5 min del Mercado Mayorista de Santa Anita"
    const val PARK_PHONES = "913 479 416 · 913 480 124"
    const val PARK_WHATSAPP_EVENTS = "+51 908 917 442"
    const val PARK_EMAILS = "atencionalcliente@vacilandia.com.pe · reservas@vacilandia.com.pe"

    const val HERO_IMAGE_URL =
        "https://images.unsplash.com/photo-1612896018599-3ba9e85cb147?auto=format&fit=crop&w=900&q=80"

    private const val URL_SLIDES =
        "https://images.unsplash.com/photo-1714496555280-fa9c37b587c0?auto=format&fit=crop&w=900&q=80"
    private const val URL_WATER_PARK =
        "https://images.unsplash.com/photo-1781735562676-46df9b3bbf59?auto=format&fit=crop&w=900&q=80"
    private const val URL_RESORT_POOL =
        "https://images.unsplash.com/photo-1516334487528-c789d53353f5?auto=format&fit=crop&w=900&q=80"
    private const val URL_KIDS_POOL =
        "https://images.unsplash.com/photo-1575429198097-0414ec08e8cd?auto=format&fit=crop&w=900&q=80"
    private const val URL_FOAM_POOL =
        "https://images.unsplash.com/photo-1576013551627-0cc20b96c2a7?auto=format&fit=crop&w=900&q=80"
    private const val URL_SUMMER_SPLASH =
        "https://images.unsplash.com/photo-1519046904884-53103b34b206?auto=format&fit=crop&w=900&q=80"
    private const val URL_CHICKEN =
        "https://images.unsplash.com/photo-1612170153139-6f881ff067e0?auto=format&fit=crop&w=900&q=80"
    private const val URL_BURGER =
        "https://images.unsplash.com/photo-1568901346375-23c9450c58cd?auto=format&fit=crop&w=900&q=80"
    private const val URL_PONY =
        "https://images.unsplash.com/photo-1553284965-83fd3e82fa5a?auto=format&fit=crop&w=900&q=80"
    private const val URL_GREEN_FARM =
        "https://images.unsplash.com/photo-1668706111332-fc6a4c53c0e2?auto=format&fit=crop&w=900&q=80"
    private const val URL_SOCCER =
        "https://images.unsplash.com/photo-1431324155629-1a6deb1dec8d?auto=format&fit=crop&w=900&q=80"
    private const val URL_EVENTS_LAWN =
        "https://images.unsplash.com/photo-1499014603094-f0c20a708009?auto=format&fit=crop&w=900&q=80"

    fun getFeaturedPromo(): PromoOffer = PromoOffer(
        id = "promo-destacada",
        ticketId = "familiar-5",
        title = "Promo Familiar 5 (Especial Web)",
        badge = "Oferta del día",
        priceLabel = "S/ 164.90",
        summary = "5 entradas Full Day + 1 pollo a la leña + papas, ensalada y gaseosa 1.5L.",
        terms = "Válido de lunes a domingo por reserva anticipada para la fecha elegida. No permite cambios ni devoluciones. Puedes combinar con código VERANO20 en tu simulación.",
        promoCodeHint = "Código demo aplicable: VERANO20 (20% dto.)",
        imageUrl = URL_CHICKEN,
        fallbackRes = R.drawable.fallback_gastronomia
    )

    fun getAttractions(): List<Attraction> = listOf(
        Attraction(
            id = "toboganes-extremos",
            name = "Circuito de Toboganes Extremos",
            category = AttractionCategory.ADRENALINA,
            tags = listOf("Adrenalina", "Multipista"),
            imageUrl = URL_SLIDES,
            fallbackRes = R.drawable.fallback_toboganes,
            gallery = listOf(
                AttractionGalleryPhoto(URL_SLIDES, R.drawable.fallback_toboganes, "Toboganes en espiral y carriles rápidos"),
                AttractionGalleryPhoto(URL_WATER_PARK, R.drawable.fallback_hero, "Vista general de torres acuáticas"),
                AttractionGalleryPhoto(URL_RESORT_POOL, R.drawable.fallback_piscina_familiar, "Piscina de recepción vigilada")
            ),
            minHeight = "1.20 m",
            depth = "1.30 m",
            waitTime = "~10 min",
            schedule = "9:30 AM – 5:30 PM",
            shortDescription = "Toboganes en espiral cerrados, abiertos y carriles multipista de alta velocidad.",
            fullDescription = "Complejo principal de toboganes diseñado para quienes buscan adrenalina en un entorno seguro y con supervisión constante de salvavidas en salida y piscina de caída.",
            highlights = listOf(
                "Carriles paralelos para carreras en familia o amigos",
                "Giros cerrados en espiral con caída refrescante",
                "Control permanente de flujo por personal salvavidas"
            ),
            safetyTips = listOf(
                "Ropa de baño obligatoria sin cierres ni botones metálicos.",
                "Respetar el turno y la señal del salvavidas antes de descender."
            )
        ),
        Attraction(
            id = "piscina-familiar",
            name = "Piscina Familiar & Zona de Descanso",
            category = AttractionCategory.FAMILIAR,
            tags = listOf("Familiar", "Todo el año"),
            imageUrl = URL_RESORT_POOL,
            fallbackRes = R.drawable.fallback_piscina_familiar,
            gallery = listOf(
                AttractionGalleryPhoto(URL_RESORT_POOL, R.drawable.fallback_piscina_familiar, "Espejo de agua principal"),
                AttractionGalleryPhoto(HERO_IMAGE_URL, R.drawable.fallback_hero, "Áreas sombreadas con parasoles"),
                AttractionGalleryPhoto(URL_FOAM_POOL, R.drawable.fallback_piscina_familiar, "Zona de reposo familiar")
            ),
            minHeight = "Libre",
            depth = "1.20 m – 1.45 m",
            waitTime = "Acceso directo",
            schedule = "9:00 AM – 5:45 PM",
            shortDescription = "Amplia piscina turquesa rodeada de sombrillas, reposeras y áreas verdes.",
            fullDescription = "Piscina recreativa central pensada para nadar y relajarse durante todo el Full Day en familia, con zonas de sombra perimetrales y vigilancia continua.",
            highlights = listOf(
                "Amplia capacidad para jóvenes y adultos",
                "Zonas sombreadas con parasoles y sillas de descanso",
                "Operativa los 365 días del año"
            ),
            safetyTips = listOf(
                "No se permite colocar mesas ni sillas propias al borde de la piscina.",
                "Usar bloqueador solar y sandalias antideslizantes."
            )
        ),
        Attraction(
            id = "piscina-infantil",
            name = "Piscina Infantil & Juegos de Agua",
            category = AttractionCategory.INFANTIL,
            tags = listOf("Infantil", "Familiar"),
            imageUrl = URL_KIDS_POOL,
            fallbackRes = R.drawable.fallback_piscina_infantil,
            gallery = listOf(
                AttractionGalleryPhoto(URL_KIDS_POOL, R.drawable.fallback_piscina_infantil, "Piscina somera para niños"),
                AttractionGalleryPhoto(URL_SUMMER_SPLASH, R.drawable.fallback_piscina_infantil, "Fuentes y chorros interactivos"),
                AttractionGalleryPhoto(URL_WATER_PARK, R.drawable.fallback_hero, "Mini toboganes temáticos")
            ),
            minHeight = "Hasta 1.25 m",
            depth = "0.45 m – 0.65 m",
            waitTime = "Sin espera",
            schedule = "9:00 AM – 5:45 PM",
            shortDescription = "Piscina de baja profundidad con mini toboganes y chorros interactivos.",
            fullDescription = "Sector adaptado especialmente para los más pequeños, con agua de baja profundidad, mini toboganes coloridos y supervisión visual directa desde las bancas familiares.",
            highlights = listOf(
                "Profundidad segura para niños pequeños",
                "Mini toboganes suaves y juegos de agua",
                "Cercanía inmediata al escenario de shows infantiles"
            ),
            safetyTips = listOf(
                "Los niños deben permanecer siempre acompañados por un adulto responsable.",
                "Uso obligatorio de pañal acuático en menores de 3 años."
            )
        ),
        Attraction(
            id = "fiesta-espuma",
            name = "Fiesta de Espuma",
            category = AttractionCategory.ADRENALINA,
            tags = listOf("Adrenalina", "Sáb y Dom"),
            imageUrl = URL_FOAM_POOL,
            fallbackRes = R.drawable.fallback_fiesta_espuma,
            gallery = listOf(
                AttractionGalleryPhoto(URL_FOAM_POOL, R.drawable.fallback_fiesta_espuma, "Plaza de espuma junto a la piscina"),
                AttractionGalleryPhoto(URL_SUMMER_SPLASH, R.drawable.fallback_fiesta_espuma, "Activación musical veraniega"),
                AttractionGalleryPhoto(URL_SLIDES, R.drawable.fallback_toboganes, "Entorno acuático principal")
            ),
            minHeight = "Libre",
            depth = "Plaza seca / 0.30 m",
            waitTime = "Turnos tarde",
            schedule = "Sáb y Dom · Tarde",
            shortDescription = "Música en vivo, animación y lluvia de espuma hipoalergénica frente a la piscina.",
            fullDescription = "Experiencia característica de los fines de semana en Vacilandia Park con música, baile grupal y cañones de espuma ligera al aire libre.",
            highlights = listOf(
                "Animación en vivo en plaza acuática",
                "Espuma suave apta para toda la familia",
                "Incluida en la Promo Macho Alfa de sábados y domingos"
            ),
            safetyTips = listOf(
                "Se recomienda participar en ropa de baño y sandalias bien sujetas.",
                "Consultar horarios de turno por altavoz del parque."
            )
        ),
        Attraction(
            id = "ecogranja-pukllay",
            name = "EcoGranja & Zona Pukllay",
            category = AttractionCategory.DEPORTES_ECO,
            tags = listOf("Eco", "Familiar"),
            imageUrl = URL_GREEN_FARM,
            fallbackRes = R.drawable.fallback_ecogranja,
            gallery = listOf(
                AttractionGalleryPhoto(URL_GREEN_FARM, R.drawable.fallback_ecogranja, "Áreas verdes y animales de granja"),
                AttractionGalleryPhoto(URL_PONY, R.drawable.fallback_ecogranja, "Espacio interactivo infantil"),
                AttractionGalleryPhoto(URL_EVENTS_LAWN, R.drawable.fallback_deportes_eventos, "Senderos rodeados de vegetación")
            ),
            minHeight = "Libre",
            depth = "Zona terrestre",
            waitTime = "Acceso directo",
            schedule = "10:00 AM – 5:00 PM",
            shortDescription = "Espacio educativo al aire libre para conocer terneros y animales de granja.",
            fullDescription = "Recorrido guiado y tranquilo entre áreas verdes diseñado para que niños y adultos interactúen respetuosamente con los animales de la EcoGranja y Zona Pukllay.",
            highlights = listOf(
                "Contacto cercano y respetuoso con animales de granja",
                "Ideal como pausa tranquila después del almuerzo",
                "Incluido con la entrada Full Day"
            ),
            safetyTips = listOf(
                "Seguir las indicaciones del cuidador y no entregar alimentos externos.",
                "Lavarse las manos al finalizar el recorrido."
            )
        ),
        Attraction(
            id = "paseos-pony",
            name = "Paseos en Pony",
            category = AttractionCategory.INFANTIL,
            tags = listOf("Infantil", "Eco"),
            imageUrl = URL_PONY,
            fallbackRes = R.drawable.fallback_ecogranja,
            gallery = listOf(
                AttractionGalleryPhoto(URL_PONY, R.drawable.fallback_ecogranja, "Circuito guiado en pony"),
                AttractionGalleryPhoto(URL_GREEN_FARM, R.drawable.fallback_ecogranja, "Entorno natural de EcoGranja"),
                AttractionGalleryPhoto(URL_EVENTS_LAWN, R.drawable.fallback_deportes_eventos, "Área familiar al aire libre")
            ),
            minHeight = "Máx. 1.35 m",
            depth = "Zona terrestre",
            waitTime = "~5 min",
            schedule = "10:30 AM – 4:30 PM",
            shortDescription = "Recorrido tranquilo y asistido en pony dentro del circuito de la EcoGranja.",
            fullDescription = "Actividad pensada para que los niños vivan un paseo seguro en pony acompañado en todo momento por personal especializado del parque.",
            highlights = listOf(
                "Paseo asistido a paso lento y seguro",
                "Supervisión individual por guía en cada vuelta",
                "Ubicado dentro de la zona verde de EcoGranja"
            ),
            safetyTips = listOf(
                "Respetar el límite de estatura y peso indicado en el módulo.",
                "Ingresar con calzado cerrado o bien ajustado."
            )
        ),
        Attraction(
            id = "shows-infantiles",
            name = "Shows Infantiles & Juegos Inflables",
            category = AttractionCategory.INFANTIL,
            tags = listOf("Infantil", "Familiar"),
            imageUrl = URL_EVENTS_LAWN,
            fallbackRes = R.drawable.fallback_deportes_eventos,
            gallery = listOf(
                AttractionGalleryPhoto(URL_EVENTS_LAWN, R.drawable.fallback_deportes_eventos, "Zona de escenario e inflables"),
                AttractionGalleryPhoto(URL_KIDS_POOL, R.drawable.fallback_piscina_infantil, "Proximidad a piscina infantil"),
                AttractionGalleryPhoto(HERO_IMAGE_URL, R.drawable.fallback_hero, "Áreas abiertas familiares")
            ),
            minHeight = "Libre",
            depth = "Zona terrestre",
            waitTime = "~5 min",
            schedule = "Funciones todo el día",
            shortDescription = "Presentaciones en vivo, concursos familiares y castillos inflables gigantes.",
            fullDescription = "Entretenimiento continuo fuera del agua con animadores en vivo, concursos participativos y juegos inflables instalados sobre césped.",
            highlights = listOf(
                "Animación musical y concursos para niños",
                "Juegos inflables gigantes en zona seca",
                "Disponible también para celebraciones de cumpleaños"
            ),
            safetyTips = listOf(
                "Ingresar a los inflables sin calzado y con ropa seca.",
                "Consultar horarios de show al ingresar al parque."
            )
        ),
        Attraction(
            id = "canchas-deportivas",
            name = "Canchas Deportivas de Fulbito",
            category = AttractionCategory.DEPORTES_ECO,
            tags = listOf("Deportes", "Familiar"),
            imageUrl = URL_SOCCER,
            fallbackRes = R.drawable.fallback_deportes_eventos,
            gallery = listOf(
                AttractionGalleryPhoto(URL_SOCCER, R.drawable.fallback_deportes_eventos, "Canchas de grass sintético"),
                AttractionGalleryPhoto(URL_EVENTS_LAWN, R.drawable.fallback_deportes_eventos, "Áreas para torneos grupales"),
                AttractionGalleryPhoto(URL_GREEN_FARM, R.drawable.fallback_ecogranja, "Zona deportiva al aire libre")
            ),
            minHeight = "Libre",
            depth = "Grass sintético",
            waitTime = "Según turno",
            schedule = "9:00 AM – 5:30 PM",
            shortDescription = "Canchas de grass sintético para partidos familiares, escolares y corporativos.",
            fullDescription = "Espacios deportivos mantenidos para seguir disfrutando fuera de las piscinas, ideales para pichangas familiares y campeonatos de empresas o colegios.",
            highlights = listOf(
                "Campos de fulbito con grass sintético",
                "Acceso incluido en tu entrada Full Day",
                "Opciones de reserva para eventos deportivos"
            ),
            safetyTips = listOf(
                "Se recomienda usar zapatillas deportivas para grass sintético.",
                "Mantener hidratación constante durante el juego."
            )
        ),
        Attraction(
            id = "feria-gastronomica",
            name = "Feria Gastronómica & MiniMarket",
            category = AttractionCategory.GASTRONOMIA,
            tags = listOf("Gastronomía", "Familiar"),
            imageUrl = URL_CHICKEN,
            fallbackRes = R.drawable.fallback_gastronomia,
            gallery = listOf(
                AttractionGalleryPhoto(URL_CHICKEN, R.drawable.fallback_gastronomia, "Pollo a la leña y combos familiares"),
                AttractionGalleryPhoto(URL_BURGER, R.drawable.fallback_gastronomia, "Hamburguesas, nuggets y snacks"),
                AttractionGalleryPhoto(URL_RESORT_POOL, R.drawable.fallback_piscina_familiar, "Comedor cerca de piscinas")
            ),
            minHeight = "Libre",
            depth = "Zona de mesas",
            waitTime = "~12 min",
            schedule = "9:30 AM – 5:45 PM",
            shortDescription = "Pollo a la leña, chancho a la caja china, parrillas, hamburguesas y MiniMarket.",
            fullDescription = "Zona gastronómica completa para disfrutar sin salir del parque. Aquí canjeas directamente los platos de tus combos Promo Personal, Kids, Familiar 5 y Macho Alfa.",
            highlights = listOf(
                "Pollo a la leña, caja china, churrasco a la parrilla y chaufa",
                "Menú infantil con hamburguesa o nuggets",
                "MiniMarket con bloqueador solar, agua y artículos de baño"
            ),
            safetyTips = listOf(
                "No se permite el ingreso con almuerzos ni bebidas externas.",
                "Presenta tu comprobante web para el canje rápido de combos."
            )
        )
    )

    fun getVisitDates(): List<VisitDateOption> {
        val shortDays = arrayOf("Dom", "Lun", "Mar", "Mié", "Jue", "Vie", "Sáb")
        val fullDays = arrayOf("Domingo", "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado")
        val months = arrayOf("Ene", "Feb", "Mar", "Abr", "May", "Jun", "Jul", "Ago", "Set", "Oct", "Nov", "Dic")

        val calendar = Calendar.getInstance()
        return (0 until 6).map { offset ->
            val dayCal = (calendar.clone() as Calendar).apply {
                add(Calendar.DAY_OF_YEAR, offset)
            }
            val dow = dayCal.get(Calendar.DAY_OF_WEEK) - 1
            val dayNum = dayCal.get(Calendar.DAY_OF_MONTH).toString()
            val monthStr = months[dayCal.get(Calendar.MONTH)]
            val isWeekend = dow == 0 || dow == 6
            val shortLabel = if (offset == 0) "Hoy" else shortDays[dow]
            val fullLabel = (if (offset == 0) "Hoy, " else "") + "${fullDays[dow]} $dayNum $monthStr"

            VisitDateOption(
                index = offset,
                shortDay = shortLabel,
                dayNumber = dayNum,
                monthLabel = monthStr,
                fullLabel = fullLabel,
                isWeekend = isWeekend
            )
        }
    }

    fun getTickets(): List<TicketItem> = listOf(
        TicketItem(
            id = "personal",
            title = "Promo Personal (Adultos)",
            subtitle = "Entrada Full Day + 1/4 Pollo a la Leña",
            badge = "Lun a Dom",
            price = 45.90,
            unitLabel = "por adulto",
            group = TicketGroup.INDIVIDUAL,
            availability = TicketAvailability.ALL_DAYS,
            imageUrl = URL_RESORT_POOL,
            fallbackRes = R.drawable.fallback_piscina_familiar,
            includes = listOf(
                "1 entrada Full Day al parque (9:00 AM – 6:00 PM)",
                "1/4 de pollo a la leña",
                "Acceso a piscinas, toboganes, EcoGranja y shows"
            ),
            conditions = "Válido de lunes a domingo únicamente para la fecha seleccionada."
        ),
        TicketItem(
            id = "vacipromo-semanal",
            title = "Vacipromo Semanal (Adultos L–V)",
            subtitle = "Tarifa web Lunes a Viernes + 1/4 Pollo a la Leña",
            badge = "Solo Lun–Vie",
            price = 29.90,
            unitLabel = "por adulto L–V",
            group = TicketGroup.INDIVIDUAL,
            availability = TicketAvailability.WEEKDAYS_ONLY,
            imageUrl = URL_SLIDES,
            fallbackRes = R.drawable.fallback_toboganes,
            includes = listOf(
                "1 entrada Full Day al parque",
                "1/4 de pollo a la leña",
                "Acceso completo a toboganes y zonas deportivas"
            ),
            conditions = "Aplica exclusivamente de lunes a viernes por reserva web."
        ),
        TicketItem(
            id = "promo-kids",
            title = "Promo Kids (Niños < 11 años)",
            subtitle = "Entrada Infantil + Hamburguesa + Juguete Sorpresa",
            badge = "Niños",
            price = 29.90,
            unitLabel = "por niño",
            group = TicketGroup.INDIVIDUAL,
            availability = TicketAvailability.ALL_DAYS,
            imageUrl = URL_BURGER,
            fallbackRes = R.drawable.fallback_piscina_infantil,
            includes = listOf(
                "1 entrada al parque (menores de 11 años)",
                "1 hamburguesa + 1 juguete sorpresa",
                "Acceso a piscina infantil, inflables y EcoGranja"
            ),
            conditions = "Válido con validación de DNI del menor al ingreso."
        ),
        TicketItem(
            id = "macho-alfa",
            title = "Promo Macho Alfa (Fin de Semana)",
            subtitle = "Entrada + 1/4 Pollo + Cerveza 473 ml + Fiesta de Espuma",
            badge = "Sáb y Dom",
            price = 34.90,
            unitLabel = "por adulto +18",
            group = TicketGroup.INDIVIDUAL,
            availability = TicketAvailability.WEEKENDS_ONLY,
            imageUrl = URL_FOAM_POOL,
            fallbackRes = R.drawable.fallback_fiesta_espuma,
            includes = listOf(
                "1 entrada Full Day al parque",
                "1/4 de pollo a la leña + 1 Cerveza Tres Cruces 473 ml",
                "Experiencia Fiesta de Espuma de fin de semana"
            ),
            conditions = "Disponible solo sábados y domingos. Bebida alcohólica para mayores de 18 años."
        ),
        TicketItem(
            id = "familiar-5",
            title = "Promo Familiar 5 (Paquete Familiar)",
            subtitle = "5 Entradas + 1 Pollo a la Leña + Papas, Ensalada y Gaseosa 1.5L",
            badge = "Paquete x5",
            price = 164.90,
            unitLabel = "≈ S/ 32.98 c/u",
            group = TicketGroup.FAMILIAR,
            availability = TicketAvailability.ALL_DAYS,
            imageUrl = URL_CHICKEN,
            fallbackRes = R.drawable.fallback_gastronomia,
            includes = listOf(
                "5 entradas Full Day al parque",
                "1 pollo a la leña entero + papas fritas + ensalada",
                "1 gaseosa de 1.5 litros"
            ),
            conditions = "Paquete familiar válido de lunes a domingo en la fecha elegida. No permite devoluciones."
        ),
        TicketItem(
            id = "preferencial",
            title = "Entrada Preferencial (65+ / CONADIS)",
            subtitle = "Tarifa social con acreditación en puerta",
            badge = "Preferencial",
            price = 5.00,
            unitLabel = "por persona",
            group = TicketGroup.ESPECIAL,
            availability = TicketAvailability.ALL_DAYS,
            imageUrl = HERO_IMAGE_URL,
            fallbackRes = R.drawable.fallback_hero,
            includes = listOf(
                "1 entrada Full Day al parque",
                "Acceso a áreas verdes, shows, piscinas y EcoGranja"
            ),
            conditions = "Presentar DNI (+65 años) o carné CONADIS vigente en ingreso."
        ),
        TicketItem(
            id = "menor-3",
            title = "Niño Menor de 3 años",
            subtitle = "Ingreso libre acompañado de adulto responsable",
            badge = "Gratis",
            price = 0.00,
            unitLabel = "con DNI",
            group = TicketGroup.ESPECIAL,
            availability = TicketAvailability.ALL_DAYS,
            imageUrl = URL_KIDS_POOL,
            fallbackRes = R.drawable.fallback_piscina_infantil,
            includes = listOf(
                "1 entrada libre para menor de 3 años",
                "Acceso a piscina infantil y EcoGranja"
            ),
            conditions = "Requiere validación de DNI y uso obligatorio de pañal acuático."
        ),
        TicketItem(
            id = "cumpleanos-libre",
            title = "Entrada Libre Cumpleaños",
            subtitle = "El cumpleañero entra gratis todo su mes",
            badge = "Cumpleañero",
            price = 0.00,
            unitLabel = "todo tu mes",
            group = TicketGroup.ESPECIAL,
            availability = TicketAvailability.ALL_DAYS,
            imageUrl = URL_EVENTS_LAWN,
            fallbackRes = R.drawable.fallback_deportes_eventos,
            includes = listOf(
                "1 entrada libre al parque para el cumpleañero",
                "Válido cualquier día durante todo el mes de cumpleaños"
            ),
            conditions = "Sujeto a presentación de DNI original. No incluye alimentos."
        )
    )

    fun getParkZones(): List<ParkZone> = listOf(
        ParkZone("z1", 1, "Ingreso & MiniMarket", "Acceso", "Validación de pase QR, DNI cumpleañeros y tienda de bloqueadores y snacks.", 0.14f, 0.78f, "feria-gastronomica"),
        ParkZone("z2", 2, "Complejo de Toboganes", "Adrenalina", "Toboganes en espiral y multipista con supervisión de salvavidas.", 0.24f, 0.25f, "toboganes-extremos"),
        ParkZone("z3", 3, "Piscina Familiar Central", "Familiar", "Espejo de agua principal con zonas de sombra y descanso.", 0.48f, 0.38f, "piscina-familiar"),
        ParkZone("z4", 4, "Piscina Infantil & Shows", "Infantil", "Piscina de baja profundidad, mini toboganes y escenario en vivo.", 0.45f, 0.68f, "piscina-infantil"),
        ParkZone("z5", 5, "Plaza Fiesta de Espuma", "Fin de semana", "Activaciones musicales y lluvia de espuma sábados y domingos.", 0.70f, 0.25f, "fiesta-espuma"),
        ParkZone("z6", 6, "Feria Gastronómica", "Gastronomía", "Pollo a la leña, caja china, parrillas, hamburguesas y canje de combos.", 0.68f, 0.56f, "feria-gastronomica"),
        ParkZone("z7", 7, "Canchas & Inflables", "Deportes", "Canchas de fulbito con grass sintético y juegos inflables gigantes.", 0.84f, 0.38f, "canchas-deportivas"),
        ParkZone("z8", 8, "EcoGranja & Paseos en Pony", "Eco", "Espacio educativo con animales de granja, Zona Pukllay y ponis.", 0.84f, 0.74f, "ecogranja-pukllay")
    )

    fun getChecklist(): List<ChecklistItem> = listOf(
        ChecklistItem("chk1", "DNI o documento de identidad físico", "Indispensable para validar promos, menores de 3 años y cumpleañeros."),
        ChecklistItem("chk2", "Ropa de baño de material sintético", "Obligatoria para ingresar a piscinas y toboganes (sin cierres metálicos)."),
        ChecklistItem("chk3", "Pañal acuático (si vienes con < 3 años)", "Requisito obligatorio para el ingreso de bebés a las piscinas."),
        ChecklistItem("chk4", "Toalla, sandalias y cambio de ropa seca", "Para moverte cómodo entre piscinas, EcoGranja y canchas."),
        ChecklistItem("chk5", "Bloqueador solar y gorra o visera", "También disponible en el MiniMarket del parque."),
        ChecklistItem("chk6", "Agua en tomatodo personal y fruta fresca", "Únicos alimentos externos permitidos (junto con papillas de bebé)."),
        ChecklistItem("chk7", "Zapatillas deportivas (opcional)", "Recomendadas si planeas jugar en las canchas de grass sintético.")
    )

    fun getPermittedRules(): List<String> = listOf(
        "Frutas frescas (manzana, fresa, etc.)",
        "Papillas y alimentos especiales para bebés",
        "Agua personal en tomatodo o botella individual",
        "Ropa de baño o prendas del mismo material",
        "Pañal acuático obligatorio en menores de 3 años",
        "Decoración ligera (banderines y globos)"
    )

    fun getProhibitedRules(): List<String> = listOf(
        "Almuerzos, snacks, dulces o bebidas externas",
        "Colocar mesas, sillas u objetos al borde de piscinas",
        "Menores de edad sin acompañamiento de un adulto",
        "Mascotas, armas o ingreso en estado etílico",
        "Fumar en áreas públicas del parque",
        "Bombardas, kabuki, pica-pica o pirotecnia"
    )

    fun getFaqs(): List<FaqItem> = listOf(
        FaqItem(
            id = "faq1",
            question = "¿Atienden los 365 días del año aunque llueva o sea feriado?",
            answer = "Sí. Vacilandia Park abre los 365 días del año, incluidos feriados y fines de semana, en horario general de 9:00 AM a 6:00 PM."
        ),
        FaqItem(
            id = "faq2",
            question = "¿Se puede ingresar con comida o bebidas?",
            answer = "Por seguridad e higiene no se permite el ingreso de almuerzos, snacks ni bebidas externas. Contamos con Feria Gastronómica (pollo a la leña, caja china, parrillas, hamburguesas) y MiniMarket. Solo se permite fruta fresca, papillas y agua personal."
        ),
        FaqItem(
            id = "faq3",
            question = "¿Qué vestimenta es obligatoria en las piscinas?",
            answer = "Es obligatorio usar ropa de baño (o prendas del mismo material sintético). Para niños menores de 3 años es obligatorio el uso de pañal acuático."
        ),
        FaqItem(
            id = "faq4",
            question = "¿Desde qué edad pagan entrada los niños?",
            answer = "Los menores de 3 años ingresan gratis presentando su DNI. Desde los 3 años cuentan con entrada pagada; para menores de 11 años está disponible la Promo Kids (S/ 29.90)."
        ),
        FaqItem(
            id = "faq5",
            question = "¿El cumpleañero entra gratis?",
            answer = "Sí. Con la Entrada Libre Cumpleaños puedes ingresar gratis cualquier día durante todo el mes de tu cumpleaños presentando tu DNI en puerta."
        ),
        FaqItem(
            id = "faq6",
            question = "¿Dónde están ubicados exactamente?",
            answer = "En Av. Las Gaviotas con Av. El Rosal, Urb. Las Gardenias, Ate, Lima — a 5 minutos del Mercado Mayorista de Santa Anita."
        ),
        FaqItem(
            id = "faq7",
            question = "¿Las entradas web permiten cambios de fecha o devoluciones?",
            answer = "Las entradas son válidas únicamente para la fecha seleccionada al reservar. No se permiten cambios de fecha ni devoluciones."
        ),
        FaqItem(
            id = "faq8",
            question = "¿Tienen paquetes para cumpleaños, colegios o empresas?",
            answer = "Sí, ofrecemos paquetes Full Day Interactivo (Vaci Kids, Vacilón, Vaci Lover y Vacilandia) desde 35 personas. Consultas al +51 908 917 442."
        )
    )
}
