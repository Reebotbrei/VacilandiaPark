package com.vacilandia.park.core.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Pool
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.ConfirmationNumber
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Pool
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.vacilandia.park.core.navigation.AppRoutes
import com.vacilandia.park.core.ui.theme.BorderDefault
import com.vacilandia.park.core.ui.theme.CoralOrange
import com.vacilandia.park.core.ui.theme.InkPrimary
import com.vacilandia.park.core.ui.theme.InkSecondary
import com.vacilandia.park.core.ui.theme.OceanBlue
import com.vacilandia.park.core.ui.theme.OceanDeep
import com.vacilandia.park.core.ui.theme.ParkRadius
import com.vacilandia.park.core.ui.theme.ShadowOcean
import com.vacilandia.park.core.ui.theme.SunYellow
import com.vacilandia.park.core.ui.theme.SunYellowPressed
import com.vacilandia.park.core.ui.theme.SurfaceCard
import com.vacilandia.park.core.ui.theme.SurfaceSubtle

/** Escala de presión compartida: se hunde al tocar y vuelve con resorte. */
@Composable
fun Modifier.pressScale(
    interaction: MutableInteractionSource,
    enabled: Boolean = true,
    pressedScale: Float = 0.965f
): Modifier {
    val pressed by interaction.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed && enabled) pressedScale else 1f,
        animationSpec = spring(dampingRatio = 0.55f, stiffness = 700f),
        label = "pressScale"
    )
    return this.graphicsLayer {
        scaleX = scale
        scaleY = scale
    }
}

/** Sombra de agua: offset + desenfoque suave, tintada de océano. */
fun Modifier.oceanShadow(elevation: Dp, shape: Shape): Modifier =
    if (elevation <= 0.dp) {
        this
    } else {
        this.shadow(
            elevation = elevation,
            shape = shape,
            clip = false,
            ambientColor = ShadowOcean.copy(alpha = 0.10f),
            spotColor = ShadowOcean.copy(alpha = 0.24f)
        )
    }

@Composable
fun ParkCard(
    modifier: Modifier = Modifier,
    backgroundColor: Color = SurfaceCard,
    borderColor: Color? = null,
    shape: Shape = RoundedCornerShape(ParkRadius.md),
    elevation: Dp = 4.dp,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val interaction = remember { MutableInteractionSource() }
    Column(
        modifier = modifier
            .pressScale(interaction, enabled = onClick != null)
            .oceanShadow(elevation, shape)
            .clip(shape)
            .background(backgroundColor)
            .let { m -> if (borderColor != null) m.border(1.5.dp, borderColor, shape) else m }
            .let { m ->
                if (onClick != null) {
                    m.clickable(
                        interactionSource = interaction,
                        indication = ripple(),
                        role = Role.Button,
                        onClick = onClick
                    )
                } else {
                    m
                }
            },
        content = content
    )
}

@Composable
fun ParkTag(
    text: String,
    textColor: Color,
    backgroundColor: Color,
    modifier: Modifier = Modifier,
    borderColor: Color? = null,
    icon: ImageVector? = null
) {
    Row(
        modifier = modifier
            .clip(CircleShape)
            .background(backgroundColor)
            .let { m -> if (borderColor != null) m.border(1.dp, borderColor, CircleShape) else m }
            .padding(horizontal = 10.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = textColor,
                modifier = Modifier.size(13.dp)
            )
        }
        Text(
            text = text,
            color = textColor,
            style = MaterialTheme.typography.labelSmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun ParkFilterChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    selectedColor: Color = OceanDeep
) {
    val interaction = remember { MutableInteractionSource() }
    val bg by animateColorAsState(
        targetValue = if (selected) selectedColor else SurfaceCard,
        animationSpec = tween(220),
        label = "chipBg"
    )
    val fg by animateColorAsState(
        targetValue = if (selected) Color.White else InkPrimary,
        animationSpec = tween(220),
        label = "chipFg"
    )
    val border by animateColorAsState(
        targetValue = if (selected) selectedColor else BorderDefault,
        animationSpec = tween(220),
        label = "chipBorder"
    )

    Row(
        modifier = modifier
            .pressScale(interaction, pressedScale = 0.93f)
            .heightIn(min = 40.dp)
            .clip(CircleShape)
            .background(bg)
            .border(1.5.dp, border, CircleShape)
            .clickable(
                interactionSource = interaction,
                indication = ripple(),
                role = Role.Tab,
                onClick = onClick
            )
            .padding(horizontal = 16.dp, vertical = 9.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AnimatedVisibility(
            visible = selected,
            enter = expandHorizontally() + fadeIn(),
            exit = shrinkHorizontally() + fadeOut()
        ) {
            Row {
                Icon(
                    imageVector = Icons.Outlined.Check,
                    contentDescription = null,
                    tint = SunYellow,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
            }
        }
        Text(
            text = label,
            color = fg,
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = if (selected) FontWeight.ExtraBold else FontWeight.SemiBold
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun ParkPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = SunYellow,
    contentColor: Color = OceanDeep,
    enabled: Boolean = true,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    elevation: Dp = 6.dp
) {
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val bg by animateColorAsState(
        targetValue = when {
            !enabled -> SurfaceSubtle
            pressed && containerColor == SunYellow -> SunYellowPressed
            else -> containerColor
        },
        animationSpec = tween(150),
        label = "btnBg"
    )
    val fg = if (enabled) contentColor else InkSecondary
    val shape = CircleShape

    Row(
        modifier = modifier
            .pressScale(interaction, enabled = enabled, pressedScale = 0.95f)
            .oceanShadow(if (enabled) elevation else 0.dp, shape)
            .heightIn(min = 52.dp)
            .clip(shape)
            .background(bg)
            .clickable(
                interactionSource = interaction,
                indication = ripple(color = contentColor),
                enabled = enabled,
                role = Role.Button,
                onClick = onClick
            )
            .padding(horizontal = 22.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (leadingIcon != null) {
            Icon(
                imageVector = leadingIcon,
                contentDescription = null,
                tint = fg,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
        }
        Text(
            text = text,
            color = fg,
            style = MaterialTheme.typography.labelLarge,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        if (trailingIcon != null) {
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = trailingIcon,
                contentDescription = null,
                tint = fg,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
fun ParkOutlineButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    borderColor: Color = OceanBlue,
    contentColor: Color = OceanBlue,
    containerColor: Color = SurfaceCard,
    leadingIcon: ImageVector? = null
) {
    val interaction = remember { MutableInteractionSource() }
    Row(
        modifier = modifier
            .pressScale(interaction, pressedScale = 0.95f)
            .heightIn(min = 52.dp)
            .clip(CircleShape)
            .background(containerColor)
            .border(BorderStroke(2.dp, borderColor), CircleShape)
            .clickable(
                interactionSource = interaction,
                indication = ripple(color = contentColor),
                role = Role.Button,
                onClick = onClick
            )
            .padding(horizontal = 20.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (leadingIcon != null) {
            Icon(
                imageVector = leadingIcon,
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
        }
        Text(
            text = text,
            color = contentColor,
            style = MaterialTheme.typography.labelLarge,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

/** Encabezado de sección: el título manda; la línea de apoyo va debajo. */
@Composable
fun SectionHeader(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,
                color = InkPrimary
            )
            if (subtitle != null) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = InkSecondary
                )
            }
        }
        if (actionLabel != null && onAction != null) {
            val interaction = remember { MutableInteractionSource() }
            Row(
                modifier = Modifier
                    .pressScale(interaction, pressedScale = 0.92f)
                    .heightIn(min = 40.dp)
                    .clip(CircleShape)
                    .background(SurfaceCard)
                    .clickable(
                        interactionSource = interaction,
                        indication = ripple(),
                        role = Role.Button,
                        onClick = onAction
                    )
                    .padding(horizontal = 14.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = actionLabel,
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.ExtraBold),
                    color = OceanBlue
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = OceanBlue,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

private data class BottomNavTab(
    val route: String,
    val label: String,
    val icon: ImageVector,
    val selectedIcon: ImageVector
)

@Composable
fun VacilandiaBottomBar(
    currentRoute: String?,
    ticketCount: Int,
    onNavigate: (String) -> Unit
) {
    val tabs = listOf(
        BottomNavTab(AppRoutes.HOME, "Inicio", Icons.Outlined.Home, Icons.Filled.Home),
        BottomNavTab(AppRoutes.ATTRACTIONS, "Atracciones", Icons.Outlined.Pool, Icons.Filled.Pool),
        BottomNavTab(AppRoutes.TICKETS, "Entradas", Icons.Outlined.ConfirmationNumber, Icons.Filled.ConfirmationNumber),
        BottomNavTab(AppRoutes.INFO_BASE, "Parque", Icons.Outlined.Info, Icons.Filled.Info)
    )

    // El contador de entradas salta cuando cambia: confirma que se agregó algo.
    val badgePop = remember { Animatable(1f) }
    LaunchedEffect(ticketCount) {
        if (ticketCount > 0) {
            badgePop.snapTo(1.45f)
            badgePop.animateTo(1f, spring(dampingRatio = 0.35f, stiffness = 500f))
        }
    }

    Column {
        HorizontalDivider(thickness = 1.dp, color = BorderDefault)
        NavigationBar(
            containerColor = SurfaceCard,
            tonalElevation = 0.dp
        ) {
            tabs.forEach { tab ->
                val isSelected = when (tab.route) {
                    AppRoutes.INFO_BASE -> currentRoute?.startsWith(AppRoutes.INFO_BASE) == true
                    else -> currentRoute == tab.route
                }
                NavigationBarItem(
                    selected = isSelected,
                    onClick = { onNavigate(tab.route) },
                    icon = {
                        BadgedBox(
                            badge = {
                                if (tab.route == AppRoutes.TICKETS && ticketCount > 0) {
                                    Badge(
                                        containerColor = CoralOrange,
                                        contentColor = Color.White,
                                        modifier = Modifier.graphicsLayer {
                                            scaleX = badgePop.value
                                            scaleY = badgePop.value
                                        }
                                    ) {
                                        Text(if (ticketCount > 9) "9+" else ticketCount.toString())
                                    }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = if (isSelected) tab.selectedIcon else tab.icon,
                                contentDescription = null
                            )
                        }
                    },
                    label = {
                        Text(
                            text = tab.label,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Medium
                            ),
                            maxLines = 1
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = OceanDeep,
                        selectedTextColor = OceanDeep,
                        indicatorColor = SunYellow,
                        unselectedIconColor = InkSecondary,
                        unselectedTextColor = InkSecondary
                    )
                )
            }
        }
    }
}
