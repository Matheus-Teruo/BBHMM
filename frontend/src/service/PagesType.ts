type PageInfo = {
  size: number;
  number: number;
  totalElements: number;
  totalPages: number;
};

export type PaginatedResponse<T> = {
  content: T[];
  page: PageInfo;
};
