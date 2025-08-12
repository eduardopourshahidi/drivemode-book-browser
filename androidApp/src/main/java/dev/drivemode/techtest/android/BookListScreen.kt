@Composable
fun BookListScreen(viewModel: BookViewModel) {
    val state by viewModel.state.collectAsState()

    when (state) {
        is BookViewModel.UiState.Idle -> Text("Search for books by subject")
        is BookViewModel.UiState.Loading -> CircularProgressIndicator()
        is BookViewModel.UiState.Success -> {
            val books = (state as BookViewModel.UiState.Success).works.books
            LazyColumn {
                items(books) { book ->
                    Row(modifier = Modifier.padding(8.dp)) {
                        AsyncImage(
                            model = book.imageUrl,
                            contentDescription = book.title,
                            modifier = Modifier.size(60.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(book.title, fontWeight = FontWeight.Bold)
                            Text(book.author, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }
        is BookViewModel.UiState.Error -> Text(
            (state as BookViewModel.UiState.Error).message,
            color = Color.Red
        )
    }
}