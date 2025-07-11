import { gql } from '@apollo/client';
import * as Apollo from '@apollo/client';
export type Maybe<T> = T | null;
export type InputMaybe<T> = Maybe<T>;
export type Exact<T extends { [key: string]: unknown }> = { [K in keyof T]: T[K] };
export type MakeOptional<T, K extends keyof T> = Omit<T, K> & { [SubKey in K]?: Maybe<T[SubKey]> };
export type MakeMaybe<T, K extends keyof T> = Omit<T, K> & { [SubKey in K]: Maybe<T[SubKey]> };
export type MakeEmpty<T extends { [key: string]: unknown }, K extends keyof T> = { [_ in K]?: never };
export type Incremental<T> = T | { [P in keyof T]?: P extends ' $fragmentName' | '__typename' ? T[P] : never };
const defaultOptions = {} as const;
/** All built-in and custom scalars, mapped to their actual values */
export type Scalars = {
  ID: { input: string; output: string; }
  String: { input: string; output: string; }
  Boolean: { input: boolean; output: boolean; }
  Int: { input: number; output: number; }
  Float: { input: number; output: number; }
  /** LocalDateTime */
  LocalDateTime: { input: any; output: any; }
  /** String that is not empty */
  NonEmptyString: { input: any; output: any; }
  /** An RFC-3339 compliant Full Time Scalar */
  Time: { input: any; output: any; }
  /** meaningless value. Necessary just for graphql limitation. Only used for output. */
  Unit: { input: any; output: any; }
};

export type AdminUser = {
  __typename?: 'AdminUser';
  email: Scalars['String']['output'];
  firstName: Scalars['String']['output'];
  id: Scalars['ID']['output'];
  isActive: Scalars['Boolean']['output'];
  lastName: Scalars['String']['output'];
  password: Scalars['String']['output'];
};

export enum AdminUserErrorCode {
  AdminUserAlreadyExists = 'ADMIN_USER_ALREADY_EXISTS',
  AdminUserNotFound = 'ADMIN_USER_NOT_FOUND'
}

export type Company = {
  __typename?: 'Company';
  buildingName?: Maybe<Scalars['String']['output']>;
  city: Scalars['String']['output'];
  createdAt: Scalars['LocalDateTime']['output'];
  id: Scalars['ID']['output'];
  name: Scalars['String']['output'];
  phoneNumber?: Maybe<Scalars['String']['output']>;
  postalCode: Scalars['String']['output'];
  prefecture: Scalars['String']['output'];
  streetAddress: Scalars['String']['output'];
  url?: Maybe<Scalars['String']['output']>;
};

export enum CompanyErrorCode {
  CompanyAlreadyExists = 'COMPANY_ALREADY_EXISTS',
  CompanyNotFound = 'COMPANY_NOT_FOUND'
}

export type CompanyQueryError = {
  __typename?: 'CompanyQueryError';
  error: CompanyErrorCode;
  message?: Maybe<Scalars['String']['output']>;
};

export type CompanyQueryOutput = CompanyQueryError | CompanyQuerySuccess;

export type CompanyQuerySuccess = {
  __typename?: 'CompanyQuerySuccess';
  company: Company;
};

export type CreateAdminUserMutationInput = {
  confirmPassword: Scalars['String']['input'];
  email: Scalars['String']['input'];
  firstName: Scalars['String']['input'];
  lastName: Scalars['String']['input'];
  password: Scalars['String']['input'];
};

export type CreateCompanyMutationError = {
  __typename?: 'CreateCompanyMutationError';
  code: CompanyErrorCode;
  message: Scalars['String']['output'];
};

export type CreateCompanyMutationInput = {
  buildingName?: InputMaybe<Scalars['NonEmptyString']['input']>;
  city: Scalars['NonEmptyString']['input'];
  name: Scalars['NonEmptyString']['input'];
  phoneNumber: Scalars['NonEmptyString']['input'];
  postalCode: Scalars['NonEmptyString']['input'];
  prefecture: Scalars['NonEmptyString']['input'];
  streetAddress: Scalars['NonEmptyString']['input'];
  url?: InputMaybe<Scalars['NonEmptyString']['input']>;
};

export type CreateCompanyMutationOutput = CreateCompanyMutationError | CreateCompanyMutationSuccess;

export type CreateCompanyMutationSuccess = {
  __typename?: 'CreateCompanyMutationSuccess';
  company: Company;
};

export type CreateDraftLessonPlanMutationError = {
  __typename?: 'CreateDraftLessonPlanMutationError';
  code: LessonPlanErrorCode;
  message: Scalars['String']['output'];
};

export type CreateDraftLessonPlanMutationInput = {
  annualMaxExecutions?: InputMaybe<Scalars['Int']['input']>;
  companyId: Scalars['ID']['input'];
  description?: InputMaybe<Scalars['NonEmptyString']['input']>;
  educations: Array<Scalars['ID']['input']>;
  grades: Array<Grade>;
  lessonPlanDates?: InputMaybe<Array<LessonPlanDateInput>>;
  lessonType?: InputMaybe<LessonType>;
  location?: InputMaybe<Scalars['NonEmptyString']['input']>;
  publish: Scalars['Boolean']['input'];
  subjects: Array<Subject>;
  title?: InputMaybe<Scalars['NonEmptyString']['input']>;
};

export type CreateDraftLessonPlanMutationOutput = CreateDraftLessonPlanMutationError | CreateDraftLessonPlanMutationSuccess;

export type CreateDraftLessonPlanMutationSuccess = {
  __typename?: 'CreateDraftLessonPlanMutationSuccess';
  result: Scalars['Unit']['output'];
};

export type CreateEducationMutationError = {
  __typename?: 'CreateEducationMutationError';
  code: EducationErrorCode;
  message: Scalars['String']['output'];
};

export type CreateEducationMutationOutput = CreateEducationMutationError | CreateEducationMutationSuccess;

export type CreateEducationMutationSuccess = {
  __typename?: 'CreateEducationMutationSuccess';
  result: Scalars['Unit']['output'];
};

export type CreateProductMutationInput = {
  description?: InputMaybe<Scalars['String']['input']>;
  name: Scalars['String']['input'];
  price: Scalars['Int']['input'];
};

export type CreateSchoolInput = {
  buildingName?: InputMaybe<Scalars['String']['input']>;
  city: Scalars['String']['input'];
  name: Scalars['String']['input'];
  phoneNumber: Scalars['String']['input'];
  postalCode: Scalars['String']['input'];
  prefecture: Prefecture;
  schoolCategory: SchoolCategory;
  streetAddress: Scalars['String']['input'];
  url?: InputMaybe<Scalars['String']['input']>;
};

export type CreateSchoolPayload = {
  __typename?: 'CreateSchoolPayload';
  errors: Array<Scalars['String']['output']>;
  school?: Maybe<School>;
};

export type CreateUploadFileInput = {
  contentType: Scalars['NonEmptyString']['input'];
  featureName: UploadFileDirectoryFeatureName;
};

export type CreateUploadFilesMutationInput = {
  fileInputs: Array<CreateUploadFileInput>;
};

export type DeleteCompanyMutationError = {
  __typename?: 'DeleteCompanyMutationError';
  code: CompanyErrorCode;
  message: Scalars['String']['output'];
};

export type DeleteCompanyMutationOutput = DeleteCompanyMutationError | DeleteCompanyMutationSuccess;

export type DeleteCompanyMutationSuccess = {
  __typename?: 'DeleteCompanyMutationSuccess';
  result: Scalars['Unit']['output'];
};

export type DeleteEducationMutationError = {
  __typename?: 'DeleteEducationMutationError';
  code: EducationErrorCode;
  message: Scalars['String']['output'];
};

export type DeleteEducationMutationOutput = DeleteEducationMutationError | DeleteEducationMutationSuccess;

export type DeleteEducationMutationSuccess = {
  __typename?: 'DeleteEducationMutationSuccess';
  result: Scalars['Unit']['output'];
};

export type DeleteProductMutationError = {
  __typename?: 'DeleteProductMutationError';
  error: ProductError;
};

export type DeleteProductMutationInput = {
  id: Scalars['ID']['input'];
};

export type DeleteProductMutationOutput = DeleteProductMutationError | DeleteProductMutationSuccess;

export type DeleteProductMutationSuccess = {
  __typename?: 'DeleteProductMutationSuccess';
  success: Scalars['Boolean']['output'];
};

export type DeleteSchoolInput = {
  id: Scalars['String']['input'];
};

export type DeleteSchoolPayload = {
  __typename?: 'DeleteSchoolPayload';
  errors: Array<Scalars['String']['output']>;
  success: Scalars['Boolean']['output'];
};

export type DeleteUploadFilesMutationInput = {
  uploadFileIDs: Array<Scalars['ID']['input']>;
};

export type DraftLessonPlan = LessonPlan & {
  __typename?: 'DraftLessonPlan';
  annualMaxExecutions?: Maybe<Scalars['Int']['output']>;
  createdAt: Scalars['LocalDateTime']['output'];
  description?: Maybe<Scalars['String']['output']>;
  id: Scalars['ID']['output'];
  lessonPlanDates?: Maybe<Array<LessonPlanDate>>;
  lessonType?: Maybe<LessonType>;
  location?: Maybe<Scalars['String']['output']>;
  title?: Maybe<Scalars['String']['output']>;
};

export enum EducationErrorCode {
  EducationAlreadyExists = 'EDUCATION_ALREADY_EXISTS',
  EducationNotFound = 'EDUCATION_NOT_FOUND'
}

export type EducationType = {
  __typename?: 'EducationType';
  displayOrder: Scalars['Int']['output'];
  grades: Array<Grade>;
  id: Scalars['ID']['output'];
  isActive: Scalars['Boolean']['output'];
  name: Scalars['String']['output'];
  subjects: Array<Subject>;
};

export type FindProductQueryError = {
  __typename?: 'FindProductQueryError';
  error: ProductError;
};

export type FindProductQueryOutput = FindProductQueryError | FindProductQuerySuccess;

export type FindProductQuerySuccess = {
  __typename?: 'FindProductQuerySuccess';
  product: Product;
};

export type FindProductsQueryError = {
  __typename?: 'FindProductsQueryError';
  error: ProductError;
};

export type FindProductsQueryOutput = FindProductsQueryError | FindProductsQuerySuccess;

export type FindProductsQuerySuccess = {
  __typename?: 'FindProductsQuerySuccess';
  products: Array<Product>;
};

export enum Grade {
  Elementary_1 = 'ELEMENTARY_1',
  Elementary_2 = 'ELEMENTARY_2',
  Elementary_3 = 'ELEMENTARY_3',
  Elementary_4 = 'ELEMENTARY_4',
  Elementary_5 = 'ELEMENTARY_5',
  Elementary_6 = 'ELEMENTARY_6',
  JuniorHigh_1 = 'JUNIOR_HIGH_1',
  JuniorHigh_2 = 'JUNIOR_HIGH_2',
  JuniorHigh_3 = 'JUNIOR_HIGH_3'
}

export type LessonPlan = {
  createdAt: Scalars['LocalDateTime']['output'];
  id: Scalars['ID']['output'];
};

export type LessonPlanDate = {
  __typename?: 'LessonPlanDate';
  endDay: Scalars['Int']['output'];
  endMonth: Scalars['Int']['output'];
  endTime: Scalars['Time']['output'];
  startDay: Scalars['Int']['output'];
  startMonth: Scalars['Int']['output'];
  startTime: Scalars['Time']['output'];
};

export type LessonPlanDateInput = {
  endDay: Scalars['Int']['input'];
  endMonth: Scalars['Int']['input'];
  endTime: Scalars['Time']['input'];
  startDay: Scalars['Int']['input'];
  startMonth: Scalars['Int']['input'];
  startTime: Scalars['Time']['input'];
};

export enum LessonPlanErrorCode {
  LessonPlanAlreadyExists = 'LESSON_PLAN_ALREADY_EXISTS',
  LessonPlanNotFound = 'LESSON_PLAN_NOT_FOUND'
}

export type LessonPlanQueryErrorOutput = {
  __typename?: 'LessonPlanQueryErrorOutput';
  code: LessonPlanErrorCode;
  message: Scalars['String']['output'];
};

export type LessonPlanQueryOutput = LessonPlanQueryErrorOutput | LessonPlanQuerySuccessOutput;

export type LessonPlanQuerySuccessOutput = {
  __typename?: 'LessonPlanQuerySuccessOutput';
  lessonPlan: LessonPlan;
};

export enum LessonType {
  Offline = 'OFFLINE',
  Online = 'ONLINE',
  OnlineAndOffline = 'ONLINE_AND_OFFLINE'
}

export type Mutation = {
  __typename?: 'Mutation';
  createAdminUser: Scalars['Unit']['output'];
  createCompany: CreateCompanyMutationOutput;
  createDraftLessonPlan: CreateDraftLessonPlanMutationOutput;
  createEducation: CreateEducationMutationOutput;
  createProduct: Scalars['Unit']['output'];
  createSchool: CreateSchoolPayload;
  createUploadFiles: Array<UploadFile>;
  deleteCompany: DeleteCompanyMutationOutput;
  deleteEducation: DeleteEducationMutationOutput;
  deleteProduct: DeleteProductMutationOutput;
  deleteSchool: DeleteSchoolPayload;
  deleteUploadFiles: Scalars['Unit']['output'];
  updateCompany: UpdateCompanyMutationOutput;
  updateEducation: UpdateEducationMutationOutput;
  updateLessonPlan: UpdateLessonPlanMutationOutput;
  updateLessonPlanStatus: UpdateLessonPlanStatusMutationOutput;
  updatePassword: UpdatePassWordMutationOutput;
  updateProduct: UpdateProductMutationOutput;
  updateSchool: UpdateSchoolPayload;
};


export type MutationCreateAdminUserArgs = {
  input: CreateAdminUserMutationInput;
};


export type MutationCreateCompanyArgs = {
  input: CreateCompanyMutationInput;
};


export type MutationCreateDraftLessonPlanArgs = {
  input: CreateDraftLessonPlanMutationInput;
};


export type MutationCreateEducationArgs = {
  name: Scalars['NonEmptyString']['input'];
};


export type MutationCreateProductArgs = {
  input: CreateProductMutationInput;
};


export type MutationCreateSchoolArgs = {
  input: CreateSchoolInput;
};


export type MutationCreateUploadFilesArgs = {
  input: CreateUploadFilesMutationInput;
};


export type MutationDeleteCompanyArgs = {
  id: Scalars['ID']['input'];
};


export type MutationDeleteEducationArgs = {
  id: Scalars['ID']['input'];
};


export type MutationDeleteProductArgs = {
  input: DeleteProductMutationInput;
};


export type MutationDeleteSchoolArgs = {
  input: DeleteSchoolInput;
};


export type MutationDeleteUploadFilesArgs = {
  input: DeleteUploadFilesMutationInput;
};


export type MutationUpdateCompanyArgs = {
  input: UpdateCompanyMutationInput;
};


export type MutationUpdateEducationArgs = {
  input: UpdateEducationMutationInput;
};


export type MutationUpdateLessonPlanArgs = {
  input: UpdateLessonPlanMutationInput;
};


export type MutationUpdateLessonPlanStatusArgs = {
  id: Scalars['ID']['input'];
};


export type MutationUpdatePasswordArgs = {
  input: UpdatePasswordMutationInput;
};


export type MutationUpdateProductArgs = {
  input: UpdateProductMutationInput;
};


export type MutationUpdateSchoolArgs = {
  input: UpdateSchoolInput;
};

export enum Prefecture {
  Aichi = 'AICHI',
  Akita = 'AKITA',
  Aomori = 'AOMORI',
  Chiba = 'CHIBA',
  Ehime = 'EHIME',
  Fukui = 'FUKUI',
  Fukuoka = 'FUKUOKA',
  Fukushima = 'FUKUSHIMA',
  Gifu = 'GIFU',
  Gunma = 'GUNMA',
  Hiroshima = 'HIROSHIMA',
  Hokkaido = 'HOKKAIDO',
  Hyogo = 'HYOGO',
  Ibaraki = 'IBARAKI',
  Ishikawa = 'ISHIKAWA',
  Iwate = 'IWATE',
  Kagawa = 'KAGAWA',
  Kagoshima = 'KAGOSHIMA',
  Kanagawa = 'KANAGAWA',
  Kochi = 'KOCHI',
  Kumamoto = 'KUMAMOTO',
  Kyoto = 'KYOTO',
  Mie = 'MIE',
  Miyagi = 'MIYAGI',
  Miyazaki = 'MIYAZAKI',
  Nagano = 'NAGANO',
  Nagasaki = 'NAGASAKI',
  Nara = 'NARA',
  Niigata = 'NIIGATA',
  Oita = 'OITA',
  Okayama = 'OKAYAMA',
  Okinawa = 'OKINAWA',
  Osaka = 'OSAKA',
  Saga = 'SAGA',
  Saitama = 'SAITAMA',
  Shiga = 'SHIGA',
  Shimane = 'SHIMANE',
  Tochigi = 'TOCHIGI',
  Tokushima = 'TOKUSHIMA',
  Tokyo = 'TOKYO',
  Tottori = 'TOTTORI',
  Toyama = 'TOYAMA',
  Wakayama = 'WAKAYAMA',
  Yamagata = 'YAMAGATA',
  Yamanashi = 'YAMANASHI'
}

export type Product = {
  __typename?: 'Product';
  description?: Maybe<Scalars['String']['output']>;
  id: Scalars['ID']['output'];
  name: Scalars['String']['output'];
  price: Scalars['Int']['output'];
};

export type ProductError = {
  __typename?: 'ProductError';
  code: ProductErrorCode;
  message: Scalars['String']['output'];
};

export enum ProductErrorCode {
  ProductNotFound = 'PRODUCT_NOT_FOUND'
}

export type PublishedLessonPlan = LessonPlan & {
  __typename?: 'PublishedLessonPlan';
  annualMaxExecutions: Scalars['Int']['output'];
  createdAt: Scalars['LocalDateTime']['output'];
  description: Scalars['String']['output'];
  id: Scalars['ID']['output'];
  lessonPlanDates: Array<LessonPlanDate>;
  lessonType: LessonType;
  location: Scalars['String']['output'];
  title: Scalars['String']['output'];
};

export type Query = {
  __typename?: 'Query';
  adminUsers: Array<AdminUser>;
  companies: Array<Company>;
  company: CompanyQueryOutput;
  educationFindByName?: Maybe<EducationType>;
  educationGetAll: Array<EducationType>;
  hello: Scalars['String']['output'];
  lessonPlan: LessonPlanQueryOutput;
  ping: Scalars['String']['output'];
  product: FindProductQueryOutput;
  products: FindProductsQueryOutput;
  school?: Maybe<School>;
  schools: Array<School>;
};


export type QueryCompanyArgs = {
  id: Scalars['ID']['input'];
};


export type QueryEducationFindByNameArgs = {
  name: Scalars['NonEmptyString']['input'];
};


export type QueryHelloArgs = {
  name?: InputMaybe<Scalars['String']['input']>;
};


export type QueryLessonPlanArgs = {
  id: Scalars['ID']['input'];
};


export type QueryProductArgs = {
  productId: Scalars['ID']['input'];
};


export type QueryProductsArgs = {
  lastId?: InputMaybe<Scalars['String']['input']>;
  limit: Scalars['Int']['input'];
  sortOrder: Scalars['String']['input'];
};


export type QuerySchoolArgs = {
  id: Scalars['String']['input'];
};

export type School = {
  __typename?: 'School';
  buildingName?: Maybe<Scalars['String']['output']>;
  city: Scalars['String']['output'];
  id: Scalars['String']['output'];
  name: Scalars['String']['output'];
  phoneNumber?: Maybe<Scalars['String']['output']>;
  postalCode: Scalars['String']['output'];
  prefecture: Scalars['String']['output'];
  schoolCategory: SchoolCategory;
  streetAddress: Scalars['String']['output'];
  url?: Maybe<Scalars['String']['output']>;
};

export enum SchoolCategory {
  Elementary = 'ELEMENTARY',
  High = 'HIGH',
  JuniorHigh = 'JUNIOR_HIGH'
}

export enum Subject {
  Arithmetic = 'ARITHMETIC',
  Art = 'ART',
  ElementaryArtAndCrafts = 'ELEMENTARY_ART_AND_CRAFTS',
  English = 'ENGLISH',
  ForeignLanguage = 'FOREIGN_LANGUAGE',
  ForeignLanguageActivities = 'FOREIGN_LANGUAGE_ACTIVITIES',
  Homemaking = 'HOMEMAKING',
  IntegratedStudies = 'INTEGRATED_STUDIES',
  Japanese = 'JAPANESE',
  LifeSkills = 'LIFE_SKILLS',
  Math = 'MATH',
  MoralEducation = 'MORAL_EDUCATION',
  Music = 'MUSIC',
  PhysicalEducation = 'PHYSICAL_EDUCATION',
  Science = 'SCIENCE',
  SocialStudies = 'SOCIAL_STUDIES'
}

export type UpdateCompanyMutationError = {
  __typename?: 'UpdateCompanyMutationError';
  code: CompanyErrorCode;
  message: Scalars['String']['output'];
};

export type UpdateCompanyMutationInput = {
  buildingName?: InputMaybe<Scalars['NonEmptyString']['input']>;
  city?: InputMaybe<Scalars['NonEmptyString']['input']>;
  id: Scalars['ID']['input'];
  name?: InputMaybe<Scalars['NonEmptyString']['input']>;
  phoneNumber: Scalars['NonEmptyString']['input'];
  postalCode?: InputMaybe<Scalars['NonEmptyString']['input']>;
  prefecture?: InputMaybe<Scalars['NonEmptyString']['input']>;
  streetAddress?: InputMaybe<Scalars['NonEmptyString']['input']>;
  url?: InputMaybe<Scalars['NonEmptyString']['input']>;
};

export type UpdateCompanyMutationOutput = UpdateCompanyMutationError | UpdateCompanyMutationSuccess;

export type UpdateCompanyMutationSuccess = {
  __typename?: 'UpdateCompanyMutationSuccess';
  result: Scalars['Unit']['output'];
};

export type UpdateEducationMutationError = {
  __typename?: 'UpdateEducationMutationError';
  code: EducationErrorCode;
  message: Scalars['String']['output'];
};

export type UpdateEducationMutationInput = {
  displayOrder?: InputMaybe<Scalars['Int']['input']>;
  id: Scalars['ID']['input'];
  isActive?: InputMaybe<Scalars['Boolean']['input']>;
  name?: InputMaybe<Scalars['NonEmptyString']['input']>;
};

export type UpdateEducationMutationOutput = UpdateEducationMutationError | UpdateEducationMutationSuccess;

export type UpdateEducationMutationSuccess = {
  __typename?: 'UpdateEducationMutationSuccess';
  result: Scalars['Unit']['output'];
};

export type UpdateLessonPlanMutationError = {
  __typename?: 'UpdateLessonPlanMutationError';
  code: LessonPlanErrorCode;
  message: Scalars['String']['output'];
};

export type UpdateLessonPlanMutationInput = {
  annualMaxExecutions?: InputMaybe<Scalars['Int']['input']>;
  description?: InputMaybe<Scalars['NonEmptyString']['input']>;
  educations: Array<Scalars['ID']['input']>;
  grades: Array<Grade>;
  id: Scalars['ID']['input'];
  images: Array<Scalars['ID']['input']>;
  lessonPlanDates?: InputMaybe<Array<LessonPlanDateInput>>;
  location?: InputMaybe<Scalars['NonEmptyString']['input']>;
  subjects: Array<Subject>;
  title?: InputMaybe<Scalars['NonEmptyString']['input']>;
  type?: InputMaybe<LessonType>;
};

export type UpdateLessonPlanMutationOutput = UpdateLessonPlanMutationError | UpdateLessonPlanMutationSuccess;

export type UpdateLessonPlanMutationSuccess = {
  __typename?: 'UpdateLessonPlanMutationSuccess';
  result: Scalars['Unit']['output'];
};

export type UpdateLessonPlanStatusMutationError = {
  __typename?: 'UpdateLessonPlanStatusMutationError';
  code: LessonPlanErrorCode;
  message: Scalars['String']['output'];
};

export type UpdateLessonPlanStatusMutationOutput = UpdateLessonPlanStatusMutationError | UpdateLessonPlanStatusMutationSuccess;

export type UpdateLessonPlanStatusMutationSuccess = {
  __typename?: 'UpdateLessonPlanStatusMutationSuccess';
  result: Scalars['Unit']['output'];
};

export type UpdatePassWordMutationError = {
  __typename?: 'UpdatePassWordMutationError';
  code: AdminUserErrorCode;
  message: Scalars['String']['output'];
};

export type UpdatePassWordMutationOutput = UpdatePassWordMutationError | UpdatePassWordMutationSuccess;

export type UpdatePassWordMutationSuccess = {
  __typename?: 'UpdatePassWordMutationSuccess';
  result: Scalars['Unit']['output'];
};

export type UpdatePasswordMutationInput = {
  adminUserId: Scalars['ID']['input'];
  confirmPassword: Scalars['String']['input'];
  password: Scalars['String']['input'];
};

export type UpdateProductMutationError = {
  __typename?: 'UpdateProductMutationError';
  error: ProductError;
};

export type UpdateProductMutationInput = {
  description?: InputMaybe<Scalars['String']['input']>;
  id: Scalars['ID']['input'];
  name?: InputMaybe<Scalars['String']['input']>;
  price?: InputMaybe<Scalars['Int']['input']>;
};

export type UpdateProductMutationOutput = UpdateProductMutationError | UpdateProductMutationSuccess;

export type UpdateProductMutationSuccess = {
  __typename?: 'UpdateProductMutationSuccess';
  product: Product;
};

export type UpdateSchoolInput = {
  buildingName?: InputMaybe<Scalars['String']['input']>;
  city?: InputMaybe<Scalars['String']['input']>;
  id: Scalars['String']['input'];
  name?: InputMaybe<Scalars['String']['input']>;
  phoneNumber?: InputMaybe<Scalars['String']['input']>;
  postalCode?: InputMaybe<Scalars['String']['input']>;
  prefecture?: InputMaybe<Prefecture>;
  schoolCategory?: InputMaybe<SchoolCategory>;
  streetAddress?: InputMaybe<Scalars['String']['input']>;
  url?: InputMaybe<Scalars['String']['input']>;
};

export type UpdateSchoolPayload = {
  __typename?: 'UpdateSchoolPayload';
  errors: Array<Scalars['String']['output']>;
  school?: Maybe<School>;
};

export enum UploadFileDirectoryFeatureName {
  RealEstate = 'REAL_ESTATE',
  Report = 'REPORT',
  WorkPlan = 'WORK_PLAN',
  WorkTemplate = 'WORK_TEMPLATE',
  WorkTicket = 'WORK_TICKET'
}

export enum UploadStatus {
  Pending = 'PENDING',
  Uploaded = 'UPLOADED'
}

export type UploadFile = {
  __typename?: 'uploadFile';
  generateDownloadFileUrl: Scalars['String']['output'];
  generateUploadFileUrl: Scalars['String']['output'];
  id: Scalars['ID']['output'];
  uploadStatus: UploadStatus;
};

export type CreateSchoolMutationVariables = Exact<{
  input: CreateSchoolInput;
}>;


export type CreateSchoolMutation = { __typename?: 'Mutation', createSchool: { __typename?: 'CreateSchoolPayload', errors: Array<string>, school?: { __typename?: 'School', id: string, name: string, schoolCategory: SchoolCategory, city: string, streetAddress: string, prefecture: string, postalCode: string, phoneNumber?: string | null, url?: string | null, buildingName?: string | null } | null } };

export type DeleteSchoolMutationVariables = Exact<{
  input: DeleteSchoolInput;
}>;


export type DeleteSchoolMutation = { __typename?: 'Mutation', deleteSchool: { __typename?: 'DeleteSchoolPayload', errors: Array<string>, success: boolean } };

export type GetCompaniesQueryVariables = Exact<{ [key: string]: never; }>;


export type GetCompaniesQuery = { __typename?: 'Query', companies: Array<{ __typename?: 'Company', id: string, name: string, buildingName?: string | null, city: string, createdAt: any, phoneNumber?: string | null, postalCode: string, prefecture: string, streetAddress: string, url?: string | null }> };

export type GetSchoolQueryVariables = Exact<{
  id: Scalars['String']['input'];
}>;


export type GetSchoolQuery = { __typename?: 'Query', school?: { __typename?: 'School', id: string, name: string, schoolCategory: SchoolCategory, city: string, streetAddress: string, prefecture: string, postalCode: string, phoneNumber?: string | null, url?: string | null, buildingName?: string | null } | null };

export type GetSchoolsQueryVariables = Exact<{ [key: string]: never; }>;


export type GetSchoolsQuery = { __typename?: 'Query', schools: Array<{ __typename?: 'School', id: string, name: string, schoolCategory: SchoolCategory, city: string, streetAddress: string, prefecture: string, postalCode: string, phoneNumber?: string | null, url?: string | null, buildingName?: string | null }> };

export type UpdateSchoolMutationVariables = Exact<{
  input: UpdateSchoolInput;
}>;


export type UpdateSchoolMutation = { __typename?: 'Mutation', updateSchool: { __typename?: 'UpdateSchoolPayload', errors: Array<string>, school?: { __typename?: 'School', id: string, name: string, schoolCategory: SchoolCategory, city: string, streetAddress: string, prefecture: string, postalCode: string, phoneNumber?: string | null, url?: string | null, buildingName?: string | null } | null } };


export const CreateSchoolDocument = gql`
    mutation createSchool($input: CreateSchoolInput!) {
  createSchool(input: $input) {
    errors
    school {
      id
      name
      schoolCategory
      city
      streetAddress
      prefecture
      postalCode
      phoneNumber
      url
      buildingName
    }
  }
}
    `;
export type CreateSchoolMutationFn = Apollo.MutationFunction<CreateSchoolMutation, CreateSchoolMutationVariables>;

/**
 * __useCreateSchoolMutation__
 *
 * To run a mutation, you first call `useCreateSchoolMutation` within a React component and pass it any options that fit your needs.
 * When your component renders, `useCreateSchoolMutation` returns a tuple that includes:
 * - A mutate function that you can call at any time to execute the mutation
 * - An object with fields that represent the current status of the mutation's execution
 *
 * @param baseOptions options that will be passed into the mutation, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options-2;
 *
 * @example
 * const [createSchoolMutation, { data, loading, error }] = useCreateSchoolMutation({
 *   variables: {
 *      input: // value for 'input'
 *   },
 * });
 */
export function useCreateSchoolMutation(baseOptions?: Apollo.MutationHookOptions<CreateSchoolMutation, CreateSchoolMutationVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useMutation<CreateSchoolMutation, CreateSchoolMutationVariables>(CreateSchoolDocument, options);
      }
export type CreateSchoolMutationHookResult = ReturnType<typeof useCreateSchoolMutation>;
export type CreateSchoolMutationResult = Apollo.MutationResult<CreateSchoolMutation>;
export type CreateSchoolMutationOptions = Apollo.BaseMutationOptions<CreateSchoolMutation, CreateSchoolMutationVariables>;
export const DeleteSchoolDocument = gql`
    mutation deleteSchool($input: DeleteSchoolInput!) {
  deleteSchool(input: $input) {
    errors
    success
  }
}
    `;
export type DeleteSchoolMutationFn = Apollo.MutationFunction<DeleteSchoolMutation, DeleteSchoolMutationVariables>;

/**
 * __useDeleteSchoolMutation__
 *
 * To run a mutation, you first call `useDeleteSchoolMutation` within a React component and pass it any options that fit your needs.
 * When your component renders, `useDeleteSchoolMutation` returns a tuple that includes:
 * - A mutate function that you can call at any time to execute the mutation
 * - An object with fields that represent the current status of the mutation's execution
 *
 * @param baseOptions options that will be passed into the mutation, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options-2;
 *
 * @example
 * const [deleteSchoolMutation, { data, loading, error }] = useDeleteSchoolMutation({
 *   variables: {
 *      input: // value for 'input'
 *   },
 * });
 */
export function useDeleteSchoolMutation(baseOptions?: Apollo.MutationHookOptions<DeleteSchoolMutation, DeleteSchoolMutationVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useMutation<DeleteSchoolMutation, DeleteSchoolMutationVariables>(DeleteSchoolDocument, options);
      }
export type DeleteSchoolMutationHookResult = ReturnType<typeof useDeleteSchoolMutation>;
export type DeleteSchoolMutationResult = Apollo.MutationResult<DeleteSchoolMutation>;
export type DeleteSchoolMutationOptions = Apollo.BaseMutationOptions<DeleteSchoolMutation, DeleteSchoolMutationVariables>;
export const GetCompaniesDocument = gql`
    query getCompanies {
  companies {
    id
    name
    buildingName
    city
    createdAt
    phoneNumber
    postalCode
    prefecture
    streetAddress
    url
  }
}
    `;

/**
 * __useGetCompaniesQuery__
 *
 * To run a query within a React component, call `useGetCompaniesQuery` and pass it any options that fit your needs.
 * When your component renders, `useGetCompaniesQuery` returns an object from Apollo Client that contains loading, error, and data properties
 * you can use to render your UI.
 *
 * @param baseOptions options that will be passed into the query, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options;
 *
 * @example
 * const { data, loading, error } = useGetCompaniesQuery({
 *   variables: {
 *   },
 * });
 */
export function useGetCompaniesQuery(baseOptions?: Apollo.QueryHookOptions<GetCompaniesQuery, GetCompaniesQueryVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useQuery<GetCompaniesQuery, GetCompaniesQueryVariables>(GetCompaniesDocument, options);
      }
export function useGetCompaniesLazyQuery(baseOptions?: Apollo.LazyQueryHookOptions<GetCompaniesQuery, GetCompaniesQueryVariables>) {
          const options = {...defaultOptions, ...baseOptions}
          return Apollo.useLazyQuery<GetCompaniesQuery, GetCompaniesQueryVariables>(GetCompaniesDocument, options);
        }
export function useGetCompaniesSuspenseQuery(baseOptions?: Apollo.SkipToken | Apollo.SuspenseQueryHookOptions<GetCompaniesQuery, GetCompaniesQueryVariables>) {
          const options = baseOptions === Apollo.skipToken ? baseOptions : {...defaultOptions, ...baseOptions}
          return Apollo.useSuspenseQuery<GetCompaniesQuery, GetCompaniesQueryVariables>(GetCompaniesDocument, options);
        }
export type GetCompaniesQueryHookResult = ReturnType<typeof useGetCompaniesQuery>;
export type GetCompaniesLazyQueryHookResult = ReturnType<typeof useGetCompaniesLazyQuery>;
export type GetCompaniesSuspenseQueryHookResult = ReturnType<typeof useGetCompaniesSuspenseQuery>;
export type GetCompaniesQueryResult = Apollo.QueryResult<GetCompaniesQuery, GetCompaniesQueryVariables>;
export const GetSchoolDocument = gql`
    query getSchool($id: String!) {
  school(id: $id) {
    id
    name
    schoolCategory
    city
    streetAddress
    prefecture
    postalCode
    phoneNumber
    url
    buildingName
  }
}
    `;

/**
 * __useGetSchoolQuery__
 *
 * To run a query within a React component, call `useGetSchoolQuery` and pass it any options that fit your needs.
 * When your component renders, `useGetSchoolQuery` returns an object from Apollo Client that contains loading, error, and data properties
 * you can use to render your UI.
 *
 * @param baseOptions options that will be passed into the query, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options;
 *
 * @example
 * const { data, loading, error } = useGetSchoolQuery({
 *   variables: {
 *      id: // value for 'id'
 *   },
 * });
 */
export function useGetSchoolQuery(baseOptions: Apollo.QueryHookOptions<GetSchoolQuery, GetSchoolQueryVariables> & ({ variables: GetSchoolQueryVariables; skip?: boolean; } | { skip: boolean; }) ) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useQuery<GetSchoolQuery, GetSchoolQueryVariables>(GetSchoolDocument, options);
      }
export function useGetSchoolLazyQuery(baseOptions?: Apollo.LazyQueryHookOptions<GetSchoolQuery, GetSchoolQueryVariables>) {
          const options = {...defaultOptions, ...baseOptions}
          return Apollo.useLazyQuery<GetSchoolQuery, GetSchoolQueryVariables>(GetSchoolDocument, options);
        }
export function useGetSchoolSuspenseQuery(baseOptions?: Apollo.SkipToken | Apollo.SuspenseQueryHookOptions<GetSchoolQuery, GetSchoolQueryVariables>) {
          const options = baseOptions === Apollo.skipToken ? baseOptions : {...defaultOptions, ...baseOptions}
          return Apollo.useSuspenseQuery<GetSchoolQuery, GetSchoolQueryVariables>(GetSchoolDocument, options);
        }
export type GetSchoolQueryHookResult = ReturnType<typeof useGetSchoolQuery>;
export type GetSchoolLazyQueryHookResult = ReturnType<typeof useGetSchoolLazyQuery>;
export type GetSchoolSuspenseQueryHookResult = ReturnType<typeof useGetSchoolSuspenseQuery>;
export type GetSchoolQueryResult = Apollo.QueryResult<GetSchoolQuery, GetSchoolQueryVariables>;
export const GetSchoolsDocument = gql`
    query getSchools {
  schools {
    id
    name
    schoolCategory
    city
    streetAddress
    prefecture
    postalCode
    phoneNumber
    url
    buildingName
  }
}
    `;

/**
 * __useGetSchoolsQuery__
 *
 * To run a query within a React component, call `useGetSchoolsQuery` and pass it any options that fit your needs.
 * When your component renders, `useGetSchoolsQuery` returns an object from Apollo Client that contains loading, error, and data properties
 * you can use to render your UI.
 *
 * @param baseOptions options that will be passed into the query, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options;
 *
 * @example
 * const { data, loading, error } = useGetSchoolsQuery({
 *   variables: {
 *   },
 * });
 */
export function useGetSchoolsQuery(baseOptions?: Apollo.QueryHookOptions<GetSchoolsQuery, GetSchoolsQueryVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useQuery<GetSchoolsQuery, GetSchoolsQueryVariables>(GetSchoolsDocument, options);
      }
export function useGetSchoolsLazyQuery(baseOptions?: Apollo.LazyQueryHookOptions<GetSchoolsQuery, GetSchoolsQueryVariables>) {
          const options = {...defaultOptions, ...baseOptions}
          return Apollo.useLazyQuery<GetSchoolsQuery, GetSchoolsQueryVariables>(GetSchoolsDocument, options);
        }
export function useGetSchoolsSuspenseQuery(baseOptions?: Apollo.SkipToken | Apollo.SuspenseQueryHookOptions<GetSchoolsQuery, GetSchoolsQueryVariables>) {
          const options = baseOptions === Apollo.skipToken ? baseOptions : {...defaultOptions, ...baseOptions}
          return Apollo.useSuspenseQuery<GetSchoolsQuery, GetSchoolsQueryVariables>(GetSchoolsDocument, options);
        }
export type GetSchoolsQueryHookResult = ReturnType<typeof useGetSchoolsQuery>;
export type GetSchoolsLazyQueryHookResult = ReturnType<typeof useGetSchoolsLazyQuery>;
export type GetSchoolsSuspenseQueryHookResult = ReturnType<typeof useGetSchoolsSuspenseQuery>;
export type GetSchoolsQueryResult = Apollo.QueryResult<GetSchoolsQuery, GetSchoolsQueryVariables>;
export const UpdateSchoolDocument = gql`
    mutation updateSchool($input: UpdateSchoolInput!) {
  updateSchool(input: $input) {
    errors
    school {
      id
      name
      schoolCategory
      city
      streetAddress
      prefecture
      postalCode
      phoneNumber
      url
      buildingName
    }
  }
}
    `;
export type UpdateSchoolMutationFn = Apollo.MutationFunction<UpdateSchoolMutation, UpdateSchoolMutationVariables>;

/**
 * __useUpdateSchoolMutation__
 *
 * To run a mutation, you first call `useUpdateSchoolMutation` within a React component and pass it any options that fit your needs.
 * When your component renders, `useUpdateSchoolMutation` returns a tuple that includes:
 * - A mutate function that you can call at any time to execute the mutation
 * - An object with fields that represent the current status of the mutation's execution
 *
 * @param baseOptions options that will be passed into the mutation, supported options are listed on: https://www.apollographql.com/docs/react/api/react-hooks/#options-2;
 *
 * @example
 * const [updateSchoolMutation, { data, loading, error }] = useUpdateSchoolMutation({
 *   variables: {
 *      input: // value for 'input'
 *   },
 * });
 */
export function useUpdateSchoolMutation(baseOptions?: Apollo.MutationHookOptions<UpdateSchoolMutation, UpdateSchoolMutationVariables>) {
        const options = {...defaultOptions, ...baseOptions}
        return Apollo.useMutation<UpdateSchoolMutation, UpdateSchoolMutationVariables>(UpdateSchoolDocument, options);
      }
export type UpdateSchoolMutationHookResult = ReturnType<typeof useUpdateSchoolMutation>;
export type UpdateSchoolMutationResult = Apollo.MutationResult<UpdateSchoolMutation>;
export type UpdateSchoolMutationOptions = Apollo.BaseMutationOptions<UpdateSchoolMutation, UpdateSchoolMutationVariables>;