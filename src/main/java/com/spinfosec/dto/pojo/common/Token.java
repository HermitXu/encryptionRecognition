package com.spinfosec.dto.pojo.common;

public class Token
{

	private String tokenId;

	public String getTokenId()
	{
		return tokenId;
	}

	public void setTokenId(String tokenId)
	{
		this.tokenId = tokenId;
	}

	@Override
	public String toString()
	{
		return "Token [tokenId=" + tokenId + "]";
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((tokenId == null) ? 0 : tokenId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Token other = (Token) obj;
		if (tokenId == null)
		{
			if (other.tokenId != null)
				return false;
		}
		else if (!tokenId.equals(other.tokenId))
			return false;
		return true;
	}
}
