/*@Composable
fun DisplayProgressBarsForCategory(candidateVotes: List<CandidateVotes>) {
    val totalVotes = candidateVotes.sumOf { it.votes }

    // Calculate candidate percentages and sort
    val rankedCandidates = candidateVotes.map { candidate ->
        val votePercentage = if (totalVotes > 0) candidate.votes.toFloat() / totalVotes else 0f
        candidate to (votePercentage * 100)
    }.sortedByDescending { it.second }

    // Get highest and lowest vote percentages
    val highestPercentage = rankedCandidates.maxOfOrNull { it.second } ?: 0f
    val lowestPercentage = rankedCandidates.minOfOrNull { it.second } ?: 0f

    rankedCandidates.forEachIndexed { index, (candidate, percentage) ->

        val targetProgress = percentage / 100
        val animatedProgress by animateFloatAsState(
            targetValue = targetProgress,
            animationSpec = tween(
                durationMillis = 1000, // Duration for smooth climbing effect
                easing = FastOutSlowInEasing // Easing function for gradual start and finish
            )
        )

        val isLeader = percentage == highestPercentage
        val isLowest = percentage == lowestPercentage

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp)
                .clip(RoundedCornerShape(16.dp))
                .shadow(3.dp)
                .border(
                    width = 2.dp,
                    color = if (isLeader) Color(0xFF4CAF50) else Color(0xFF000000),
                    shape = RoundedCornerShape(16.dp)
                ),
            colors = CardDefaults.cardColors(containerColor = if (isLeader) Color(0xFFE0F7FA) else Color(0xFFE0E0E0))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = candidate.name,
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                        color = Color(0xFF333333)
                    )
                    Text(
                        text = "${candidate.votes} votes (${String.format("%.2f%%", percentage)})",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = if (isLeader) FontWeight.Bold else FontWeight.Normal
                        ),
                        color = Color(0xFF4F4F4F)
                    )
                }

                LinearProgressIndicator(
                    progress = animatedProgress, // Smoothly animated progress
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(14.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFFFFFFF)),
                    color = when {
                        isLeader -> Color(0xFF4CAF50)
                        isLowest -> Color(0xFFF44336)
                        else -> Color(0xFFFFC107)
                    },
                    trackColor = Color(0xFFFFFFFF).copy(alpha = 0.3f),
                )
            }
        }
    }
}


// Function to shuffle candidates based on votes
fun shuffleCandidates(candidates: List<CandidateVotes>): List<CandidateVotes> {
    return candidates.sortedBy { it.votes }.shuffled() // Sort first (if needed) and then shuffle
}




fun DrawScope.drawCustomShadow() {
    val shadowColor = Color.Black.copy(alpha = 0.2f)
    val cornerRadius = 12.dp.toPx()

    // Draw shadow all around the card
    drawRoundRect(
        color = shadowColor,
        topLeft = Offset(x = -8.dp.toPx(), y = -8.dp.toPx()),
        size = Size(
            width = this.size.width + 16.dp.toPx(),
            height = this.size.height + 16.dp.toPx()
        ),
        cornerRadius = CornerRadius(cornerRadius)
    )

    // Add a second shadow layer for more depth
    drawRoundRect(
        color = shadowColor.copy(alpha = 0.1f),
        topLeft = Offset(x = -4.dp.toPx(), y = -4.dp.toPx()),
        size = Size(
            width = this.size.width + 8.dp.toPx(),
            height = this.size.height + 8.dp.toPx()
        ),
        cornerRadius = CornerRadius(cornerRadius)
        )
}
*/