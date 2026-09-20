import {
  entityConfirmDeleteButtonSelector,
  entityCreateButtonSelector,
  entityCreateCancelButtonSelector,
  entityCreateSaveButtonSelector,
  entityDeleteButtonSelector,
  entityDetailsBackButtonSelector,
  entityDetailsButtonSelector,
  entityEditButtonSelector,
  entityTableSelector,
} from '../../support/entity';

describe('ProductRole e2e test', () => {
  const productRolePageUrl = '/product-role';
  let username: string;
  let password: string;
  const productRoleSample = {};

  let productRole;
  let product;
  let party;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/products',
      body: {
        name: 'always venom fluff',
        internalName: 'cautious millet',
        brandName: 'dependency',
        variant: 'hyena very',
        sku: 'nor nor now',
        introductionDate: '2023-12-14T10:28:58.437Z',
        releaseDate: '2023-12-14T21:18:35.358Z',
        quantityIncluded: 6513.58,
        piecesIncluded: 1083,
        weight: 12233.82,
        height: 14083.29,
        width: 10742.1,
        depth: 26515.29,
        smallImageUrl: 'besmirch',
        mediumImageUrl: 'babyish while',
        largeImageUrl: 'oh',
        detailImageUrl: 'mmm',
        originalImageUrl: 'galoshes intensely teammate',
        altImageUrl1: 'into',
        altImageUrl2: 'arid valentine',
        altImageUrl3: 'versus rightfully',
        altImageUrl4: 'unwritten',
        altImageUrl5: 'gadzooks',
        altImageUrl6: 'fairly',
        videoUrl: 'lest smoothly',
        quantity: 18486.71,
        price: 24113.94,
        cgst: 9208.27,
        igst: 14132.86,
        sgst: 29024.86,
        cgstPercentage: 5210.39,
        igstPercentage: 2653.9,
        sgstPercentage: 64.92,
        totalPrice: 1523.19,
        description: 'leading submitter once',
        longDescription: 'through pulverize',
        info: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=',
        isVirtual: true,
        isVariant: false,
        requireInventory: true,
        returnable: true,
        isPopular: true,
        isBanner: true,
        bannerUrl: 'exactly kosher',
      },
    }).then(({ body }) => {
      product = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/parties',
      body: {
        isOrganisation: false,
        organisationName: 'wisely zesty likewise',
        organisationShortName: 'outside whose drat',
        salutation: 'owlishly',
        firstName: 'Reba',
        middleName: 'sedately',
        lastName: 'Fritsch',
        gender: 'FEMALE',
        birthDate: '2023-12-14',
        primaryPhone: 'slowly or draw',
        primaryEmail: 'gymnast violent after',
        isTemporaryPassword: false,
        logoImageUrl: 'without',
        profileImageUrl: 'leap wherever',
        notes: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=',
        postalAddress: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=',
      },
    }).then(({ body }) => {
      party = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/product-roles+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/product-roles').as('postEntityRequest');
    cy.intercept('DELETE', '/api/product-roles/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/products', {
      statusCode: 200,
      body: [product],
    });

    cy.intercept('GET', '/api/parties', {
      statusCode: 200,
      body: [party],
    });

    cy.intercept('GET', '/api/role-types', {
      statusCode: 200,
      body: [],
    });
  });

  afterEach(() => {
    if (productRole) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/product-roles/${productRole.id}`,
      }).then(() => {
        productRole = undefined;
      });
    }
  });

  afterEach(() => {
    if (product) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/products/${product.id}`,
      }).then(() => {
        product = undefined;
      });
    }
    if (party) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/parties/${party.id}`,
      }).then(() => {
        party = undefined;
      });
    }
  });

  it('ProductRoles menu should load ProductRoles page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('product-role');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ProductRole').should('exist');
    cy.location('pathname').should('eq', productRolePageUrl);
  });

  describe('ProductRole page', () => {
    it('should have translated page title', () => {
      cy.visit(productRolePageUrl);
      cy.getEntityHeading('ProductRole').should('not.contain', 'snpApp.productRole.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(productRolePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ProductRole page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${productRolePageUrl}/new`);
        cy.getEntityCreateUpdateHeading('ProductRole');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productRolePageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/product-roles',
          body: {
            ...productRoleSample,
            product,
            party,
          },
        }).then(({ body }) => {
          productRole = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/product-roles+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/product-roles?page=0&size=20>; rel="last",<http://localhost/api/product-roles?page=0&size=20>; rel="first"',
              },
              body: [productRole],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(productRolePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ProductRole page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('productRole');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productRolePageUrl);
      });

      it('edit button click should load edit ProductRole page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ProductRole');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productRolePageUrl);
      });

      it('edit button click should load edit ProductRole page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ProductRole');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productRolePageUrl);
      });

      it('last delete button click should delete instance of ProductRole', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('productRole').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', productRolePageUrl);

        productRole = undefined;
      });
    });
  });

  describe('new ProductRole page', () => {
    beforeEach(() => {
      cy.visit(productRolePageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ProductRole');
    });

    it('should create an instance of ProductRole', () => {
      cy.get(`[data-cy="product"]`).select(1);
      cy.get(`[data-cy="party"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        productRole = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', productRolePageUrl);
    });
  });
});
