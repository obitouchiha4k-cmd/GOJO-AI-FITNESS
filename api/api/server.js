
const express = require("express");
const cors = require("cors");

const app = express();

app.use(cors());
app.use(express.json({ limit: "10mb" }));

app.get("/", (req, res) => {
  res.json({
    status: "OK",
    service: "GOJO AI FITNESS API"
  });
});

app.post("/analyze-food", async (req, res) => {
  try {
    const { imageBase64, mimeType } = req.body || {};

    if (!process.env.GEMINI_API_KEY) {
      return res.status(500).json({
        error: "Server API key is not configured"
      });
    }

    if (
      typeof imageBase64 !== "string" ||
      !["image/jpeg", "image/png", "image/webp"].includes(mimeType)
    ) {
      return res.status(400).json({
        error: "Valid imageBase64 and mimeType are required"
      });
    }

    if (
      imageBase64.length > 8000000 ||
      !/^[A-Za-z0-9+/]+={0,2}$/.test(imageBase64)
    ) {
      return res.status(413).json({
        error: "Image is too large or invalid"
      });
    }

    const { GoogleGenAI } = await import("@google/genai");
    const ai = new GoogleGenAI({
      apiKey: process.env.GEMINI_API_KEY
    });

    const response = await ai.models.generateContent({
      model: "gemini-flash-latest",
      contents: [
        {
          inlineData: {
            mimeType,
            data: imageBase64
          }
        },
        `Analyze this food photo for a fitness tracking app.

Estimate the visible foods and portion sizes.
Do not pretend estimates are exact.
If the food is unclear, say so.

Return ONLY valid JSON in this format:
{
  "foods": [
    {
      "name": "food name",
      "portion": "estimated portion",
      "calories": 0,
      "protein": 0,
      "carbs": 0,
      "fats": 0
    }
  ],
  "totalCalories": 0,
  "totalProtein": 0,
  "totalCarbs": 0,
  "totalFats": 0,
  "notes": "Estimates only. Please confirm food and portion."
}

All nutrient values must be numbers in grams, except calories which are kcal.`
      ],
      config: {
        responseMimeType: "application/json"
      }
    });

    const result = JSON.parse(response.text);

    return res.json({
      success: true,
      estimate: result
    });

  } catch (error) {
    console.error("Food analysis error:", error);

    return res.status(500).json({
      error: "Food analysis failed. Please try again."
    });
  }
});

const PORT = process.env.PORT || 3000;

app.listen(PORT, "0.0.0.0", () => {
  console.log(`GOJO AI FITNESS API running on port ${PORT}`);
});
