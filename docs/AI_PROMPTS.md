# OpSpot — AI Prompts

Use these prompts with **Gemini**, **ChatGPT**, or any AI assistant to generate JSON data ready to import into OpSpot.

> **Instructions:** Copy the prompt below, paste it into your AI tool, and paste the response directly into the OpSpot **Import JSON** modal.

---

## 🗓 Events & Hackathons

```
Find 10 real upcoming tech events or hackathons in India for 2025–2026.
Look for events similar to these examples:
- https://www.scaler.com/school-of-technology/meta-pytorch-hackathon
- https://economictimes.indiatimes.com/et-ai-hackathon/2nd-edition
- https://aidevsummit.co/hackathon/

Return ONLY a valid JSON object — no markdown fences, no explanation — using this exact structure:

{
  "items": [
    {
      "title": "...",
      "description": "one-line description of the event",
      "organizer": "...",
      "eventType": "HACKATHON",
      "workMode": "OFFLINE",
      "city": "...",
      "themes": ["AI", "Web3", "HealthTech"],
      "problemStatements": [
        "Build an AI-powered crop disease detection system for farmers",
        "Design a decentralized identity verification platform"
      ],
      "startDate": "2026-03-01",
      "endDate": "2026-03-02",
      "registrationDeadline": "2026-02-20",
      "studentAllowed": true,
      "professionalAllowed": false,
      "minTeamSize": 2,
      "maxTeamSize": 4,
      "registrationLink": "https://...",
      "phases": [
        {
          "phaseName": "Online Round",
          "startDate": "2026-03-01",
          "endDate": "2026-03-07",
          "location": "Online",
          "phaseOrder": 1
        },
        {
          "phaseName": "Grand Finale",
          "startDate": "2026-03-20",
          "endDate": "2026-03-21",
          "location": "Mumbai",
          "phaseOrder": 2
        }
      ]
    }
  ]
}

Rules:
- eventType must be EVENT or HACKATHON
- workMode must be ONLINE, OFFLINE, or HYBRID
- themes must be a JSON array of strings
- problemStatements must be a JSON array of strings (can be empty [] for non-hackathon events)
- phases array is optional for non-hackathon events
- All dates must be in YYYY-MM-DD format
- registrationLink must be unique per item and a real URL
```

---

## 💼 Jobs

```
Find 10 real software engineering job openings in India for 2025–2026.
Return ONLY a valid JSON object — no markdown fences, no explanation — using this exact structure:

{
  "items": [
    {
      "title": "Senior Software Engineer – Java/Spring Boot",
      "company": "...",
      "location": "Bangalore",
      "workMode": "HYBRID",
      "skills": "Java,Spring Boot,Kafka,MySQL",
      "experienceMin": 3,
      "experienceMax": 6,
      "salaryRange": "25-40 LPA",
      "description": "one-line description of the role",
      "jobLink": "https://..."
    }
  ]
}

Rules:
- workMode must be ONLINE, OFFLINE, or HYBRID
- skills must be a comma-separated string
- experienceMin and experienceMax must be integers (years)
- jobLink must be unique per item and a real URL
```

---

## 📚 Course Offers

```
Find 10 real discounted or free tech courses on platforms like Udemy, Coursera, or Scaler for 2025–2026.
Return ONLY a valid JSON object — no markdown fences, no explanation — using this exact structure:

{
  "items": [
    {
      "title": "...",
      "platform": "Udemy",
      "topic": "Kafka",
      "originalPrice": 3999.00,
      "discountedPrice": 499.00,
      "isFree": false,
      "validTill": "2026-05-31",
      "description": "one-line description of the course",
      "offerLink": "https://..."
    }
  ]
}

Rules:
- isFree must be true or false
- If isFree is true, set both originalPrice and discountedPrice to 0
- validTill can be null if the offer has no expiry
- offerLink must be unique per item and a real URL
- All dates must be in YYYY-MM-DD format
```

---

## 🏃 Marathons (Bangalore)

```
Find 10 real upcoming marathons or running events happening in Bangalore, India for 2025–2026.
Return ONLY a valid JSON object — no markdown fences, no explanation — using this exact structure:

{
  "items": [
    {
      "title": "...",
      "description": "one-line description of the event",
      "organizer": "...",
      "date": "2026-03-15",
      "registrationDeadline": "2026-03-01",
      "entryFee": 800.00,
      "isFree": false,
      "location": "Kanteerava Stadium, Bangalore",
      "distanceCategories": ["5K", "10K", "21K", "42K"],
      "registrationLink": "https://..."
    }
  ]
}

Rules:
- All events must be in Bangalore only
- isFree must be true or false; if true set entryFee to 0
- distanceCategories must be a JSON array of strings (e.g. "5K", "10K", "21K", "42K")
- All dates must be in YYYY-MM-DD format
- registrationLink must be unique per item and a real URL
```

---

## 🤖 AI Tool Offers

```
Find 10 real AI tool free trials or limited-time discounted offers available for developers in 2025–2026.
Return ONLY a valid JSON object — no markdown fences, no explanation — using this exact structure:

{
  "items": [
    {
      "toolName": "Cursor",
      "offerTitle": "Cursor Pro – 3 Months Free for Students",
      "isFree": true,
      "forProfessionals": false,
      "validTill": "2026-06-30",
      "description": "one-line description of the offer",
      "offerLink": "https://..."
    }
  ]
}

Rules:
- isFree must be true or false
- forProfessionals must be true or false
- validTill can be null if the offer has no expiry
- offerLink must be unique per item and a real URL
- All dates must be in YYYY-MM-DD format
```
