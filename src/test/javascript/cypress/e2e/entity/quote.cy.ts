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

describe('Quote e2e test', () => {
  const quotePageUrl = '/quote';
  let username: string;
  let password: string;
  const quoteSample = { issueDate: '2023-12-14T09:40:52.202Z' };

  let quote;
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
      url: '/api/parties',
      body: {
        isOrganisation: true,
        organisationName: 'whoever',
        organisationShortName: 'gloss',
        salutation: 'beard querulous exce',
        firstName: 'Aubrey',
        middleName: 'nor sticky obscure',
        lastName: 'Harber',
        gender: 'OTHER',
        birthDate: '2023-12-14',
        primaryPhone: 'gah',
        primaryEmail: 'along',
        isTemporaryPassword: false,
        logoImageUrl: 'surprisingly quirkily consequently',
        profileImageUrl: 'queasily',
        notes: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=',
        postalAddress: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=',
      },
    }).then(({ body }) => {
      party = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/quotes+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/quotes').as('postEntityRequest');
    cy.intercept('DELETE', '/api/quotes/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/quote-types', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/cust-requests', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/parties', {
      statusCode: 200,
      body: [party],
    });

    cy.intercept('GET', '/api/statuses', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/product-stores', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/sales-channels', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/uoms', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/uom-conversions', {
      statusCode: 200,
      body: [],
    });
  });

  afterEach(() => {
    if (quote) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/quotes/${quote.id}`,
      }).then(() => {
        quote = undefined;
      });
    }
  });

  afterEach(() => {
    if (party) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/parties/${party.id}`,
      }).then(() => {
        party = undefined;
      });
    }
  });

  it('Quotes menu should load Quotes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('quote');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Quote').should('exist');
    cy.location('pathname').should('eq', quotePageUrl);
  });

  describe('Quote page', () => {
    it('should have translated page title', () => {
      cy.visit(quotePageUrl);
      cy.getEntityHeading('Quote').should('not.contain', 'snpApp.quote.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(quotePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Quote page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${quotePageUrl}/new`);
        cy.getEntityCreateUpdateHeading('Quote');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', quotePageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/quotes',
          body: {
            ...quoteSample,
            party,
          },
        }).then(({ body }) => {
          quote = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/quotes+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/quotes?page=0&size=20>; rel="last",<http://localhost/api/quotes?page=0&size=20>; rel="first"',
              },
              body: [quote],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(quotePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Quote page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('quote');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', quotePageUrl);
      });

      it('edit button click should load edit Quote page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Quote');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', quotePageUrl);
      });

      it('edit button click should load edit Quote page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Quote');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', quotePageUrl);
      });

      it('last delete button click should delete instance of Quote', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('quote').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', quotePageUrl);

        quote = undefined;
      });
    });
  });

  describe('new Quote page', () => {
    beforeEach(() => {
      cy.visit(quotePageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Quote');
    });

    it('should create an instance of Quote', () => {
      cy.get(`[data-cy="name"]`).type('microchip inasmuch');
      cy.get(`[data-cy="name"]`).should('have.value', 'microchip inasmuch');

      cy.get(`[data-cy="offerNo"]`).type('since tackle yowza');
      cy.get(`[data-cy="offerNo"]`).should('have.value', 'since tackle yowza');

      cy.get(`[data-cy="issueDate"]`).type('2023-12-14T15:38');
      cy.get(`[data-cy="issueDate"]`).blur();
      cy.get(`[data-cy="issueDate"]`).should('have.value', '2023-12-14T15:38');

      cy.get(`[data-cy="validFromDate"]`).type('2023-12-14T22:01');
      cy.get(`[data-cy="validFromDate"]`).blur();
      cy.get(`[data-cy="validFromDate"]`).should('have.value', '2023-12-14T22:01');

      cy.get(`[data-cy="validThruDate"]`).type('2023-12-14T11:53');
      cy.get(`[data-cy="validThruDate"]`).blur();
      cy.get(`[data-cy="validThruDate"]`).should('have.value', '2023-12-14T11:53');

      cy.get(`[data-cy="description"]`).type('napkin pilot');
      cy.get(`[data-cy="description"]`).should('have.value', 'napkin pilot');

      cy.get(`[data-cy="offerValue"]`).type('20797.37');
      cy.get(`[data-cy="offerValue"]`).should('have.value', '20797.37');

      cy.get(`[data-cy="remarks"]`).type('gaseous tribe');
      cy.get(`[data-cy="remarks"]`).should('have.value', 'gaseous tribe');

      cy.get(`[data-cy="party"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        quote = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', quotePageUrl);
    });
  });
});
