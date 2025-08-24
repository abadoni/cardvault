import React, { useState } from "react";
import axios from "axios";

function App() {
	// Form state
	const [cardholderName, setCardholderName] = useState("");
	const [cardNumber, setCardNumber] = useState("");

	// Search state
	const [searchQuery, setSearchQuery] = useState("");
	const [results, setResults] = useState([]);

	// Messages
	const [message, setMessage] = useState(null);
	const [cardNumberError, setCardNumberError] = useState(""); // Inline validation


	// Remove non-digits while typing
	const sanitizeNumber = (value) => value.replace(/\D/g, "");

	// Inline validation for PAN
	const validateCardNumber = () => {
		if (cardNumber.length < 12 || cardNumber.length > 19) {
			setCardNumberError("Card number must be 12-19 digits");
			return false;
		}
		setCardNumberError("");
		return true;
	};

	// --- Handlers ---

	// Card number input
	const handleCardNumberChange = (e) => {
		setCardNumber(sanitizeNumber(e.target.value));
		setCardNumberError("");
	};

	// Add Card
	const handleAddCard = async (e) => {
		e.preventDefault();

		// Validate before sending
		if (!validateCardNumber()) return;

		try {
			await axios.post("http://localhost:8080/api/cards", {
				cardHolder: cardholderName, 
				pan: cardNumber,
			});

			setMessage({ type: "success", text: "Card added successfully!" });
			setCardholderName("");
			setCardNumber("");
		} catch (err) {
			const errorText =
				err.response?.data?.error || "Failed to add card. Please try again.";
			setMessage({ type: "error", text: errorText });
		}
	};

	// Search Cards
	const handleSearch = async () => {
		const query = sanitizeNumber(searchQuery);
		setSearchQuery(query);

		if (!query) return;

		try {
			const response = await axios.get(
				`http://localhost:8080/api/cards/search?query=${query}`
			);
			setResults(response.data);

			if (response.data.length === 0) {
				setMessage({ type: "error", text: "No cards found" });
			} else {
				setMessage(null);
			}
		} catch (err) {
			const errorText =
					err.response?.data?.error || "Failed to search cards.";
			setMessage({ type: "error", text: errorText });
		}
	};

	// --- Render ---
	return (
		<div style={{ margin: "40px", fontFamily: "Arial, sans-serif" }}>
			<h1>Card Vault</h1>

			{/* Success/Error Messages */}
			{message && (
				<div
					style={{
						marginBottom: "20px",
						padding: "10px",
						borderRadius: "5px",
						color: "#fff",
						backgroundColor: message.type === "success" ? "green" : "red",
					}}
				>
					{message.text}
				</div>
			)}

			{/* Add Card Form */}
			<form onSubmit={handleAddCard} style={{ marginBottom: "20px" }}>
				<div style={{ marginBottom: "10px" }}>
					<label>Cardholder Name: </label>
					<input
						type="text"
						value={cardholderName}
						onChange={(e) => setCardholderName(e.target.value)}
						required
					/>
				</div>
				<div style={{ marginBottom: "10px" }}>
					<label>Card Number (PAN): </label>
					<input
						type="text"
						value={cardNumber}
						onChange={handleCardNumberChange}
						onBlur={validateCardNumber}
						required
					/>
					{cardNumberError && (
						<div style={{ color: "red", fontWeight: "bold" }}>
							{cardNumberError}
						</div>
					)}
				</div>
				<button type="submit">Add Card</button>
			</form>

			{/* Search */}
			<div style={{ marginBottom: "20px" }}>
				<label>Search PAN: </label>
				<input
					type="text"
					value={searchQuery}
					onChange={(e) => setSearchQuery(sanitizeNumber(e.target.value))}
				/>
				<button onClick={handleSearch}>Search</button>
			</div>

			{/* Results Table */}
			<div>
				<h2>Results</h2>
				<table border="1" cellPadding="10">
					<thead>
						<tr>
							<th>Cardholder Name</th>
							<th>Masked PAN</th>
							<th>Created Time</th>
						</tr>
					</thead>
					<tbody>
						{results.length > 0 ? (
							results.map((card, idx) => (
								<tr key={idx}>
									<td>{card.cardHolderName}</td>
									<td>{card.maskedCardNumber}</td>
									<td>{card.createdAt}</td>
								</tr>
							))
						) : (
							<tr>
								<td colSpan="3">No results</td>
							</tr>
						)}
					</tbody>
				</table>
			</div>
		</div>
	);
}

export default App;
