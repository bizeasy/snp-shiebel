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

describe('QuoteType e2e test', () => {
  const quoteTypePageUrl = '/quote-type';
  let username: string;
  let password: string;
  const quoteTypeSample = { name: 'consequently and' };

  let quoteType;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/quote-types+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/quote-types').as('postEntityRequest');
    cy.intercept('DELETE', '/api/quote-types/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (quoteType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/quote-types/${quoteType.id}`,
      }).then(() => {
        quoteType = undefined;
      });
    }
  });

  it('QuoteTypes menu should load QuoteTypes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('quote-type');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('QuoteType').should('exist');
    cy.location('pathname').should('eq', quoteTypePageUrl);
  });

  describe('QuoteType page', () => {
    it('should have translated page title', () => {
      cy.visit(quoteTypePageUrl);
      cy.getEntityHeading('QuoteType').should('not.contain', 'snpApp.quoteType.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(quoteTypePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create QuoteType page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${quoteTypePageUrl}/new`);
        cy.getEntityCreateUpdateHeading('QuoteType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', quoteTypePageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/quote-types',
          body: quoteTypeSample,
        }).then(({ body }) => {
          quoteType = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/quote-types+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [quoteType],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(quoteTypePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details QuoteType page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('quoteType');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', quoteTypePageUrl);
      });

      it('edit button click should load edit QuoteType page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('QuoteType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', quoteTypePageUrl);
      });

      it('edit button click should load edit QuoteType page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('QuoteType');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', quoteTypePageUrl);
      });

      it('last delete button click should delete instance of QuoteType', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('quoteType').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', quoteTypePageUrl);

        quoteType = undefined;
      });
    });
  });

  describe('new QuoteType page', () => {
    beforeEach(() => {
      cy.visit(quoteTypePageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('QuoteType');
    });

    it('should create an instance of QuoteType', () => {
      cy.get(`[data-cy="name"]`).type('ha');
      cy.get(`[data-cy="name"]`).should('have.value', 'ha');

      cy.get(`[data-cy="description"]`).type('bathhouse until');
      cy.get(`[data-cy="description"]`).should('have.value', 'bathhouse until');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        quoteType = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', quoteTypePageUrl);
    });
  });
});
