# Bill Splitting API / Project

A robust backend system designed to split bills, manage shared events, handle participants, and track payments (including PIX keys support) among groups of users.

---

## 🚀 Features

- **User Management & Authentication:** Secure registration, verification, and token management.
- **PIX Integration:** Store and manage PIX keys and bank accounts for easy reimbursements.
- **Event Organization:** Create events, invite users, and track financial statuses per event.
- **Bill Tracking:** Register expenses, assign payers, manage split participants, and handle itemized discounts.

---

## 📊 Database Architecture

The system uses a relational database structure designed to handle users, events, bills, participants, and discounts. You can check the complete structure in the [Entity Relationship Diagram](./entity_relation_diagram.mermaid) file, or visualize the relationship summary below:

- **Users & Authentication:** Managed via `USERS`, `TOKENS`, and `PIXES`.
- **Events & Collaboration:** Handled through `EVENTS`, `EVENT_USER`, and `EVENT_INVITATION`.
- **Financials:** Controlled by `BILLS`, `PARTICIPANTS`, `DISCOUNTS`, and `BILL_DISCOUNTS`.

---

## License
This project is open-source and licensed under the MIT License.
[LICENSE](LICENSE)