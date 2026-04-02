package com.harsh.FinanceDashboard.Finance.Dashboard.enums;

public enum Category {

    // ── Income ──────────────────────────────
    REVENUE,            // core business revenue
    SERVICE_INCOME,     // income from services rendered
    INVESTMENT_RETURN,  // returns from investments
    LOAN_RECEIVED,      // loans taken by company
    GRANT,              // government or external grants
    OTHER_INCOME,       // miscellaneous income

    // ── Operational Expenses ─────────────────
    SALARIES,           // employee salaries
    RENT,               // office rent
    UTILITIES,          // electricity, water, internet
    OFFICE_SUPPLIES,    // stationery, printer etc
    MAINTENANCE,        // office/equipment maintenance

    // ── Business Expenses ────────────────────
    MARKETING,          // ads, campaigns, promotions
    TRAVEL,             // business travel
    CLIENT_ENTERTAINMENT, // client meals, events
    SOFTWARE,           // SaaS tools, licenses
    HARDWARE,           // laptops, servers, equipment

    // ── Financial ────────────────────────────
    TAX,                // government taxes
    LOAN_REPAYMENT,     // repaying loans
    INSURANCE,          // company insurance
    AUDIT_LEGAL,        // auditing and legal fees

    // ── People ───────────────────────────────
    RECRUITMENT,        // hiring costs
    TRAINING,           // employee training
    EMPLOYEE_BENEFITS,  // health, perks etc

    OTHER_EXPENSE       // miscellaneous expense
}
