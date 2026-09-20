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

describe('CatalogueCategory e2e test', () => {
  const catalogueCategoryPageUrl = '/catalogue-category';
  let username: string;
  let password: string;
  const catalogueCategorySample = {};

  let catalogueCategory;
  let catalogue;
  let productCategory;

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
      url: '/api/catalogues',
      body: {
        name: 'address and',
        description: 'after innocently',
        sequenceNo: 14206,
        imagePath: 'ack next publication',
        mobileImagePath: 'sympathetically responsible',
        altImage1: 'quizzically depart',
        altImage2: 'emerge',
        altImage3: 'as',
        bannerUrl: 'uh-huh horst',
        isBanner: true,
      },
    }).then(({ body }) => {
      catalogue = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/product-categories',
      body: {
        name: 'towards',
        description: 'eek daughter times',
        longDescription: 'hm',
        attribute: 'cultivated lieu',
        sequenceNo: 14153,
        imageUrl: 'lest',
        altImageUrl: 'um ultimately',
        bannerUrl: 'import zowie scrap',
        largeImageUrl: 'morning',
        altLargeImageUrl: 'after zebra procurement',
        info: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=',
        isBanner: false,
      },
    }).then(({ body }) => {
      productCategory = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/catalogue-categories+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/catalogue-categories').as('postEntityRequest');
    cy.intercept('DELETE', '/api/catalogue-categories/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/catalogues', {
      statusCode: 200,
      body: [catalogue],
    });

    cy.intercept('GET', '/api/product-categories', {
      statusCode: 200,
      body: [productCategory],
    });

    cy.intercept('GET', '/api/catalogue-category-types', {
      statusCode: 200,
      body: [],
    });
  });

  afterEach(() => {
    if (catalogueCategory) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/catalogue-categories/${catalogueCategory.id}`,
      }).then(() => {
        catalogueCategory = undefined;
      });
    }
  });

  afterEach(() => {
    if (catalogue) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/catalogues/${catalogue.id}`,
      }).then(() => {
        catalogue = undefined;
      });
    }
    if (productCategory) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/product-categories/${productCategory.id}`,
      }).then(() => {
        productCategory = undefined;
      });
    }
  });

  it('CatalogueCategories menu should load CatalogueCategories page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('catalogue-category');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('CatalogueCategory').should('exist');
    cy.location('pathname').should('eq', catalogueCategoryPageUrl);
  });

  describe('CatalogueCategory page', () => {
    it('should have translated page title', () => {
      cy.visit(catalogueCategoryPageUrl);
      cy.getEntityHeading('CatalogueCategory').should('not.contain', 'snpApp.catalogueCategory.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(catalogueCategoryPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create CatalogueCategory page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${catalogueCategoryPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('CatalogueCategory');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', catalogueCategoryPageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/catalogue-categories',
          body: {
            ...catalogueCategorySample,
            catalogue,
            productCategory,
          },
        }).then(({ body }) => {
          catalogueCategory = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/catalogue-categories+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [catalogueCategory],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(catalogueCategoryPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details CatalogueCategory page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('catalogueCategory');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', catalogueCategoryPageUrl);
      });

      it('edit button click should load edit CatalogueCategory page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CatalogueCategory');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', catalogueCategoryPageUrl);
      });

      it('edit button click should load edit CatalogueCategory page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CatalogueCategory');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', catalogueCategoryPageUrl);
      });

      it('last delete button click should delete instance of CatalogueCategory', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('catalogueCategory').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', catalogueCategoryPageUrl);

        catalogueCategory = undefined;
      });
    });
  });

  describe('new CatalogueCategory page', () => {
    beforeEach(() => {
      cy.visit(catalogueCategoryPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('CatalogueCategory');
    });

    it('should create an instance of CatalogueCategory', () => {
      cy.get(`[data-cy="sequenceNo"]`).type('12625');
      cy.get(`[data-cy="sequenceNo"]`).should('have.value', '12625');

      cy.get(`[data-cy="catalogue"]`).select(1);
      cy.get(`[data-cy="productCategory"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        catalogueCategory = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', catalogueCategoryPageUrl);
    });
  });
});
